import { beforeEach, describe, expect, expectTypeOf, it } from "vitest";
import useForm from "@/composables/useForm";

const {
	validateTextInput,
	validateNumericInput,
	handleInputUpdate,
	handleNumericInputUpdate,
	handleMediaUpdate,
	handleSelectUpdate,
	handleConditionUpdate,
	removeNullsFromImages,
	isValidForm,
} = useForm();

const createFormData = () => {
	const emptyProperty = { value: null, valid: false };
	return {
		textField: Object.create(emptyProperty),
		numberField: Object.create(emptyProperty),
		media: Object.create(emptyProperty),
		selectField: Object.create(emptyProperty),
		condition: Object.create(emptyProperty),
	};
};

const mockDOMEvent = (id, value) => ({
	target: {
		id,
		value,
	},
});

describe("validateTextInput", () => {
	let mockFormData = null;
	beforeEach(() => (mockFormData = createFormData()));

	it("should change the valid property of a text in the formdata", () => {
		const event = mockDOMEvent("textField", "foo");
		expect(mockFormData.textField.valid).toBe(false);

		validateTextInput(event, mockFormData);
		expect(mockFormData.textField.valid).toBe(true);
	});

	it("should not change the valid property of a text when the event has an empty string", () => {
		const event = mockDOMEvent("textField", "");
		expect(mockFormData.textField.valid).toBe(false);

		validateTextInput(event, mockFormData);
		expect(mockFormData.textField.valid).toBe(false);
	});
});

describe("validateNumericInput", () => {
	let mockFormData = null;
	beforeEach(() => (mockFormData = createFormData()));

	it("should change the valid property of a number in the formdata", () => {
		const event = mockDOMEvent("numberField", "1");
		expect(mockFormData.numberField.valid).toBe(false);

		validateNumericInput(event, mockFormData);
		expect(mockFormData.numberField.valid).toBe(true);
	});

	it("should not change the valid property of a number when the event has a zero or a negative", () => {
		const event = mockDOMEvent("numberField", "0");
		expect(mockFormData.numberField.valid).toBe(false);

		validateNumericInput(event, mockFormData);
		expect(mockFormData.numberField.valid).toBe(false);

		event.target.value = "-1";
		validateNumericInput(event, mockFormData);
		expect(mockFormData.numberField.valid).toBe(false);
	});
});

describe("handleInputUpdate", () => {
	it("should change the value property of a text in the formdata", () => {
		const mockFormData = createFormData();
		const event = mockDOMEvent("textField", "Foo");
		expect(mockFormData.textField.value).toBeNull();

		handleInputUpdate(event, mockFormData);
		expect(mockFormData.textField.value).toBe(event.target.value);
	});
});

describe("handleNumericInputUpdate", () => {
	let mockFormData = null;
	beforeEach(() => (mockFormData = createFormData()));

	it("should change the value property of a number in the formdata", () => {
		const event = mockDOMEvent("numberField", "1");
		expect(mockFormData.numberField.value).toBeNull();

		handleNumericInputUpdate(event, mockFormData);
		expect(mockFormData.numberField.value).toBe(Number(event.target.value));

		event.target.value = "-1";
		handleNumericInputUpdate(event, mockFormData);
		expect(mockFormData.numberField.value).toBe(Number(event.target.value));
	});

	it("should not change the value property of a number if an invalid number is provided", () => {
		const event = mockDOMEvent("numberField", "foo");
		expect(mockFormData.numberField.value).toBeNull();

		handleNumericInputUpdate(event, mockFormData);
		expect(mockFormData.numberField.value).toBeNull();

		event.target.value = "";
		handleNumericInputUpdate(event, mockFormData);
		expect(mockFormData.numberField.value).toBe(Number(event.target.value));
	});
});

describe("handleMediaUpdate", () => {
	let mockFormData = null;
	beforeEach(() => (mockFormData = createFormData()));

	it("should change the value of media list when passed an array with info", () => {
		const media = ["1", "2"];
		expect(mockFormData.media.value).toBeNull();

		handleMediaUpdate(media, mockFormData);
		expectTypeOf(mockFormData.media.value).toBeArray();
		expect(mockFormData.media.value.length).toBe(2);
	});

	it("should change the value of media list when passed an empty array", () => {
		const media = [];
		expect(mockFormData.media.value).toBeNull();

		handleMediaUpdate(media, mockFormData);
		expectTypeOf(mockFormData.media.value).toBeArray();
		expect(mockFormData.media.value.length).toBe(0);
	});

	it("should change the valid property of media to true if provided a non empty array", () => {
		const media = ["1", "2"];

		handleMediaUpdate(media, mockFormData);
		expect(mockFormData.media.valid).toBe(true);
	});

	it("should not change the valid property of media to true if provided an empty array", () => {
		const media = [];

		expect(mockFormData.media.valid).toBe(false);
		handleMediaUpdate(media, mockFormData);
		expect(mockFormData.media.valid).toBe(false);
	});
});

describe("handleSelectUpdate", () => {
	let mockFormData = null;
	let event = null;
	beforeEach(() => {
		mockFormData = createFormData();
		event = Object.create({
			target: {
				id: "selectField",
				options: [{ dataset: { id: 10 } }, { dataset: { id: 20 } }],
				selectedIndex: 0,
			},
		});
	});

	it("should change the value property of a select in the formdata", () => {
		expect(mockFormData.selectField.value).toBeNull();

		handleSelectUpdate(event, mockFormData);
		expect(mockFormData.selectField.value).toBe(10);

		event.target.selectedIndex = 1;
		handleSelectUpdate(event, mockFormData);
		expect(mockFormData.selectField.value).toBe(20);
	});

	it("should change the valid property of a select in the formdata", () => {
		handleSelectUpdate(event, mockFormData);
		expect(mockFormData.selectField.valid).toBe(true);
	});
});

describe("handleConditionUpdate", () => {
	let mockFormData = null;
	beforeEach(() => (mockFormData = createFormData()));

	it("should change the value and valid property of condition in the formdata", () => {
		const event = { rating: 1 };
		expect(mockFormData.condition.value).toBeNull();
		expect(mockFormData.condition.valid).toBe(false);

		handleConditionUpdate(event, mockFormData);

		expect(mockFormData.condition.value).toBe(1);
		expect(mockFormData.condition.valid).toBe(true);
	});

	it("should not change the value and valid property of condition if negativ or invalid inputs are provided", () => {
		const event = { rating: 0 };
		expect(mockFormData.condition.value).toBeNull();
		expect(mockFormData.condition.valid).toBe(false);

		handleConditionUpdate(event, mockFormData);
		expect(mockFormData.condition.value).toBeNull();
		expect(mockFormData.condition.valid).toBe(false);

		event.rating = -1;
		handleConditionUpdate(event, mockFormData);
		expect(mockFormData.condition.value).toBeNull();

		event.rating = "foo";
		handleConditionUpdate(event, mockFormData);
		expect(mockFormData.condition.value).toBeNull();
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

describe("isValidForm", () => {
	let mockFormData = null;
	beforeEach(() => (mockFormData = createFormData()));

	it("should return true all fields of the form are valid", () => {
		const mockFormData = createFormData();
		Object.keys(mockFormData).forEach(field => {
			mockFormData[field].valid = true;
		});

		expect(isValidForm(mockFormData)).toBe(true);
	});

	it("should return false if at least one field is invalid", () => {
		const mockFormData = createFormData();
		Object.keys(mockFormData).forEach(field => {
			mockFormData[field].valid = true;
		});

		mockFormData.numberField.valid = false;
		expect(isValidForm(mockFormData)).toBe(false);
	});
});
