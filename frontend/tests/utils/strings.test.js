import { describe, expect, it } from "vitest";
import {
	capitalizeFirstLetter,
	getArticleOfSentence,
	extractFirstWordsFromText,
	getSaleStatusColor,
	addParamsToURL,
} from "@/utils/strings.js";

const TEXT =
	"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque scelerisque tempor velit a consequat. Nunc.";

describe("function capitalizeFistLetter", () => {
	it("should return 'Foo' when provided 'foo'", () => {
		expect(capitalizeFirstLetter("foo")).toBe("Foo");
	});

	it("should return 'Foo' when provided 'Foo'", () => {
		expect(capitalizeFirstLetter("Foo")).toBe("Foo");
	});

	it("should return '.foo' when provided '.foo'", () => {
		expect(capitalizeFirstLetter(".foo")).toBe(".foo");
	});

	it("should throw an error when a single character is provided", () => {
		expect(() => {
			capitalizeFirstLetter("h");
		}).toThrow();
	});

	it("should throw an error when an incorrect type is provided", () => {
		expect(() => {
			capitalizeFirstLetter(-1);
		}).toThrow();

		expect(() => {
			capitalizeFirstLetter({});
		}).toThrow();

		expect(() => {
			capitalizeFirstLetter();
		}).toThrow();
	});
});

describe("function getArticleOfSentence", () => {
	it("should return 'a' when provided a 1 and a consonant", () => {
		expect(getArticleOfSentence(1, "g")).toBe("a");
	});

	it("should return 'an' when provided a 1 and a vowel", () => {
		expect(getArticleOfSentence(1, "e")).toBe("an");
	});

	it("should work when the provided number is a string", () => {
		expect(getArticleOfSentence("1", "x")).toBe("a");
		expect(getArticleOfSentence("1", "I")).toBe("an");
	});

	it("should return a number as string when the first param is greater than 1", () => {
		expect(getArticleOfSentence(3, "g")).toBe("3");
		expect(getArticleOfSentence(4, "E")).toBe("4");
		expect(getArticleOfSentence(4)).toBe("4");
	});

	it("should do return 'a' if the second param is incorrect type or value", () => {
		expect(getArticleOfSentence(1, "@")).toBe("a");
		expect(getArticleOfSentence(1, {})).toBe("a");
		expect(getArticleOfSentence(1, 1)).toBe("a");
		expect(getArticleOfSentence(1, null)).toBe("a");
	});

	it("should throw an error if the fist param is not a number", () => {
		expect(() => {
			getArticleOfSentence({});
		}).toThrow();
		expect(() => {
			getArticleOfSentence("+");
		}).toThrow();
		expect(() => {
			getArticleOfSentence(null);
		}).toThrow();
	});
});

describe("function extractFirstWordsFromText", () => {
	it("should return only the first x words of a text", () => {
		expect(extractFirstWordsFromText(TEXT, 2)).toBe("Lorem ipsum");
		expect(extractFirstWordsFromText(TEXT, 5)).toBe(
			"Lorem ipsum dolor sit amet,"
		);
	});

	it("should return the unmodified text if its number of words is less than n", () => {
		expect(extractFirstWordsFromText(TEXT, 16)).toBe(TEXT);
	});

	it("should return undefined if the text is not a string", () => {
		expect(extractFirstWordsFromText()).toBeUndefined();
		expect(extractFirstWordsFromText(null, 1)).toBeUndefined();
		expect(extractFirstWordsFromText(1, 1)).toBeUndefined();
	});

	it("should return undefined if n is not a number", () => {
		expect(extractFirstWordsFromText(TEXT, "1")).toBeUndefined();
		expect(extractFirstWordsFromText(TEXT, {})).toBeUndefined();
		expect(extractFirstWordsFromText(TEXT, null)).toBeUndefined();
	});

	it("should return undefined if n is not a positive number", () => {
		expect(extractFirstWordsFromText(TEXT, 0)).toBeUndefined();
		expect(extractFirstWordsFromText(TEXT, -1)).toBeUndefined();
	});
});

describe("function getSaleStatusColor", () => {
	const fulfilled = "Fulfilled";
	const canceled = "Cancelled";
	const pending = "Pending";

	it("should return an orange css color class when passed pending", () => {
		expect(getSaleStatusColor(pending)).toContain("text");
		expect(getSaleStatusColor(pending)).toContain("orange");
	});

	it("should return an green css color class when passed fulfilled", () => {
		expect(getSaleStatusColor(fulfilled)).toContain("text");
		expect(getSaleStatusColor(fulfilled)).toContain("green");
	});

	it("should return an red css color class when passed canceled", () => {
		expect(getSaleStatusColor(canceled)).toContain("text");
		expect(getSaleStatusColor(canceled)).toContain("red");
	});

	it("should return undefined if any other string or value is passed", () => {
		expect(getSaleStatusColor("foo")).toBeUndefined();
		expect(getSaleStatusColor(0)).toBeUndefined();
		expect(getSaleStatusColor([])).toBeUndefined();
		expect(getSaleStatusColor(null)).toBeUndefined();
	});
});

describe("function addParamsToURL", () => {
	const BASE_URL = "https://example.com/route";
	const params = {
		name: "foo",
		index: 2,
		foo: "bar",
	};

	it("should return the same string when no params are provided", () => {
		expect(addParamsToURL(BASE_URL)).toBe(BASE_URL);
	});

	it("should return the same string when params are not an object", () => {
		expect(addParamsToURL(BASE_URL, "foo")).toBe(BASE_URL);
		expect(addParamsToURL(BASE_URL, 0)).toBe(BASE_URL);
		expect(addParamsToURL(BASE_URL, true)).toBe(BASE_URL);
		expect(addParamsToURL(BASE_URL, [])).toBe(BASE_URL);
		expect(addParamsToURL(BASE_URL, null)).toBe(BASE_URL);
	});

	it("should return the url + the params in key=value format", () => {
		const result = addParamsToURL(BASE_URL, params);
		expect(result).toContain("name=foo");
		expect(result).toContain("index=2");
		expect(result).toContain("foo=bar");
	});

	it("should return a url with a '?' character", () => {
		expect(addParamsToURL(BASE_URL, params)).toContain("?");
	});

	it("should return a url with at least one '&' when provided multiple params", () => {
		expect(addParamsToURL(BASE_URL, params)).toContain("&");
	});

	it("should return a url with no '&' when provided a single param", () => {
		const result = addParamsToURL(BASE_URL, { param: 0 });
		expect(result.includes("&")).toBe(false);
	});

	it("should not return a url with a '&' at the end", () => {
		const result = addParamsToURL(BASE_URL, params);
		expect(result.slice(-1) === "&").toBe(false);
	});
});
