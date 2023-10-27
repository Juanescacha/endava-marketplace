import { describe, expect, it } from "vitest";
import {
	capitalizeFirstLetter,
	getArticleOfSentence,
	extractFirstWordsFromText,
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
