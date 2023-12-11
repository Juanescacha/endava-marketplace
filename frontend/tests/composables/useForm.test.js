import { beforeEach, describe, expect, expectTypeOf, it, vi } from "vitest";
import useForm from "@/composables/useForm";

const {
	formData,
	decorateFormData,
	validateTextInput,
	validateNumericInput,
	handleTextInput,
	handleNumericInput,
	handleMediaUpdate,
	handleSelectUpdate,
	handleConditionUpdate,
	removeNullsFromImages,
	preventNegativeNumber,
	isValidForm,
} = useForm();

const clearFormData = () => {
	const emptyProperty = { value: null, valid: false };
	formData.textField = Object.create(emptyProperty);
	formData.numberField = Object.create(emptyProperty);
	formData.media = Object.create(emptyProperty);
	formData.selectField = Object.create(emptyProperty);
	formData.condition = Object.create(emptyProperty);
};

const mockDOMEvent = (id, value) => ({
	target: {
		id,
		value,
	},
});

describe("decorateFormData", () => {
	it("should throw an error if the parameter is not an array", () => {
		expect(() => decorateFormData()).toThrowError();
		expect(() => decorateFormData("foo")).toThrowError();
		expect(() => decorateFormData(4)).toThrowError();
		expect(() => decorateFormData(null)).toThrowError();
		expect(() => decorateFormData({ foo: "bar" })).toThrowError();
	});

	it("should throw an error if the passed array doesn't have only strings", () => {
		expect(() => decorateFormData(["foo", 1])).toThrowError();
		expect(() => decorateFormData([null, "foo"])).toThrowError();
		expect(() => decorateFormData(["foo", {}])).toThrowError();
		expect(() => decorateFormData(["foo", undefined])).toThrowError();
		expect(() => decorateFormData(["foo", []])).toThrowError();
	});

	it("should throw an error if the strings can't be a proper object key", () => {
		expect(() => decorateFormData(["1foo"])).toThrowError();
		expect(() => decorateFormData(["foo bar"])).toThrowError();
		expect(() => decorateFormData(["foo-bar"])).toThrowError();
	});

	it("should populate the formData with keys equivalent to the passed strings", () => {
		const keys = ["foo", "bar"];
		decorateFormData(keys);
		expect(formData).toHaveProperty(keys[0]);
		expect(formData).toHaveProperty(keys[1]);
		expect(formData.foo).toHaveProperty("value");
		expect(formData.foo).toHaveProperty("valid");
	});
});

describe("validateTextInput", () => {
	beforeEach(() => clearFormData());

	it("should set the valid property to true when no min or max are set", () => {
		validateTextInput("textField", "");
		expect(formData.textField.valid).toBe(true);

		validateTextInput("textField", "foo");
		expect(formData.textField.valid).toBe(true);
	});

	it("should set the valid property to true when the input is within min and max", () => {
		formData.textField.min = 1;
		formData.textField.max = 3;

		validateTextInput("textField", "f");
		expect(formData.textField.valid).toBe(true);
		validateTextInput("textField", "fo");
		expect(formData.textField.valid).toBe(true);
		validateTextInput("textField", "foo");
		expect(formData.textField.valid).toBe(true);
	});

	it("should set valid property to false when the value doesn't have the correct length", () => {
		formData.textField.valid = true;
		formData.textField.min = 1;
		validateTextInput("textField", "");
		expect(formData.textField.valid).toBe(false);

		formData.textField.valid = true;
		formData.textField.max = 5;
		validateTextInput("textField", "Sample");
		expect(formData.textField.valid).toBe(false);
	});
});

describe("validateNumericInput", () => {
	beforeEach(() => clearFormData());

	it("should set the valid property to true when no min or max are set", () => {
		expect(formData.numberField.valid).toBe(false);

		validateNumericInput("numberField", "1");
		expect(formData.numberField.valid).toBe(true);
	});

	it("should set the valid property to true when the input is within min and max", () => {
		formData.numberField.min = 1;
		formData.numberField.max = 3;

		validateNumericInput("numberField", "1");
		expect(formData.numberField.valid).toBe(true);
		validateNumericInput("numberField", "2");
		expect(formData.numberField.valid).toBe(true);
		validateNumericInput("numberField", "3");
		expect(formData.numberField.valid).toBe(true);
	});

	it("should set valid property to false when the value doesn't have the correct length", () => {
		formData.numberField.valid = true;
		formData.numberField.min = 1;
		validateNumericInput("numberField", "0");
		expect(formData.numberField.valid).toBe(false);

		formData.numberField.valid = true;
		formData.numberField.max = 5;
		validateNumericInput("numberField", "6");
		expect(formData.numberField.valid).toBe(false);
	});
});

describe("handleTextInput", () => {
	it("should change the value property of a text in the formdata", () => {
		clearFormData();
		const event = mockDOMEvent("textField", "Foo");
		expect(formData.textField.value).toBeNull();

		handleTextInput(event);
		expect(formData.textField.value).toBe(event.target.value);

		event.target.value = "";
		handleTextInput(event);
		expect(formData.textField.value).toBe(event.target.value);

		event.target.value = null;
		handleTextInput(event);
		expect(formData.textField.value).toBe(event.target.value);
	});
});

describe("handleNumericInput", () => {
	beforeEach(() => clearFormData());

	it("should change the value property of a number in the formdata", () => {
		const event = mockDOMEvent("numberField", "1");
		expect(formData.numberField.value).toBeNull();

		handleNumericInput(event);
		expect(formData.numberField.value).toBe(Number(event.target.value));

		event.target.value = "-1";
		handleNumericInput(event);
		expect(formData.numberField.value).toBe(Number(event.target.value));
	});

	it("should not change the value property of a number if an invalid number is provided", () => {
		const event = mockDOMEvent("numberField", "foo");
		expect(formData.numberField.value).toBeNull();

		handleNumericInput(event);
		expect(formData.numberField.value).toBeNull();

		event.target.value = "";
		handleNumericInput(event);
		expect(formData.numberField.value).toBe(Number(event.target.value));

		event.target.value = null;
		handleNumericInput(event);
		expect(formData.numberField.value).toBe(Number(event.target.value));
	});
});

describe("handleMediaUpdate", () => {
	beforeEach(() => clearFormData());

	it("should change the value of media list when passed an array with info", () => {
		const media = ["1", "2"];
		expect(formData.media.value).toBeNull();

		handleMediaUpdate(media);
		expectTypeOf(formData.media.value).toBeArray();
		expect(formData.media.value.length).toBe(2);
	});

	it("should change the value of media list when passed an empty array", () => {
		const media = [];
		expect(formData.media.value).toBeNull();

		handleMediaUpdate(media);
		expectTypeOf(formData.media.value).toBeArray();
		expect(formData.media.value.length).toBe(0);
	});

	it("should change the valid property of media to true if provided a non empty array", () => {
		const media = ["1", "2"];

		handleMediaUpdate(media);
		expect(formData.media.valid).toBe(true);
	});

	it("should not change the valid property of media to true if provided an empty array", () => {
		const media = [];

		expect(formData.media.valid).toBe(false);
		handleMediaUpdate(media);
		expect(formData.media.valid).toBe(false);
	});
});

describe("handleSelectUpdate", () => {
	let event = null;
	beforeEach(() => {
		clearFormData();
		event = Object.create({
			target: {
				id: "selectField",
				options: [{ dataset: { id: 10 } }, { dataset: { id: 20 } }],
				selectedIndex: 0,
			},
		});
	});

	it("should change the value property of a select in the formdata", () => {
		expect(formData.selectField.value).toBeNull();

		handleSelectUpdate(event);
		expect(formData.selectField.value).toBe(10);

		event.target.selectedIndex = 1;
		handleSelectUpdate(event);
		expect(formData.selectField.value).toBe(20);
	});

	it("should change the valid property of a select in the formdata", () => {
		handleSelectUpdate(event);
		expect(formData.selectField.valid).toBe(true);
	});
});

describe("handleConditionUpdate", () => {
	beforeEach(() => clearFormData());

	it("should change the value and valid property of condition in the formdata", () => {
		const event = { rating: 1 };
		expect(formData.condition.value).toBeNull();
		expect(formData.condition.valid).toBe(false);

		handleConditionUpdate(event);

		expect(formData.condition.value).toBe(1);
		expect(formData.condition.valid).toBe(true);
	});

	it("should not change the value and valid property of condition if negativ or invalid inputs are provided", () => {
		const event = { rating: 0 };
		expect(formData.condition.value).toBeNull();
		expect(formData.condition.valid).toBe(false);

		handleConditionUpdate(event);
		expect(formData.condition.value).toBeNull();
		expect(formData.condition.valid).toBe(false);

		event.rating = -1;
		handleConditionUpdate(event);
		expect(formData.condition.value).toBeNull();

		event.rating = "foo";
		handleConditionUpdate(event);
		expect(formData.condition.value).toBeNull();
	});
});

describe("removeNullsFromImages", () => {
	it("should remove the null on the last position of a list", () => {
		const mocks = [["foo", "bar", null], ["foo", null], [null]];

		expect(removeNullsFromImages(mocks[0]).length).toBe(2);
		expect(removeNullsFromImages(mocks[1]).length).toBe(1);
		expect(removeNullsFromImages(mocks[2]).length).toBe(0);
	});

	it("should return a copy of the list if it doesn't have a null", () => {
		const mocks = ["foo", "bar", "null"];

		const resultList = removeNullsFromImages(mocks);
		expect(resultList.length).toBe(3);
		expect(resultList[2]).toBe("null");
	});

	it("should not alter the original list", () => {
		const mocks = ["foo", "bar", null];

		removeNullsFromImages(mocks);
		expect(mocks.length).toBe(3);
		expect(mocks[2]).toBeNull();
	});
});

describe("preventNegativeNumber", () => {
	const getMockDOMEvent = () => ({
		key: "-",
		preventDefault: vi.fn(),
	});

	it("should call the event.preventDefault function when the key is a '-'", () => {
		const event = getMockDOMEvent();
		const preventDefaultSpy = vi.spyOn(event, "preventDefault");

		preventNegativeNumber(event);

		expect(preventDefaultSpy).toHaveBeenCalled();
	});
	it("should not call the event.preventDefault function when the key is different from '-'", () => {
		const event = getMockDOMEvent();
		const preventDefaultSpy = vi.spyOn(event, "preventDefault");

		event.key = "a";
		preventNegativeNumber(event);

		event.key = "+";
		preventNegativeNumber(event);

		event.key = "1";
		preventNegativeNumber(event);

		expect(preventDefaultSpy).not.toHaveBeenCalled();
	});

	it("should not call the event.preventDefault function when the event doesn't have a 'key' property", () => {
		const event = getMockDOMEvent();
		const preventDefaultSpy = vi.spyOn(event, "preventDefault");

		delete event.key;
		preventNegativeNumber(event);

		expect(preventDefaultSpy).not.toHaveBeenCalled();
	});
});

describe("isValidForm", () => {
	beforeEach(() => clearFormData());

	it("should return true all fields of the form are valid", () => {
		Object.keys(formData).forEach(field => {
			formData[field].valid = true;
		});

		expect(isValidForm()).toBe(true);
	});

	it("should return false if at least one field is invalid", () => {
		Object.keys(formData).forEach(field => {
			formData[field].valid = true;
		});

		formData.numberField.valid = false;
		expect(isValidForm()).toBe(false);
	});
});
