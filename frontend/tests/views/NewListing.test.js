import {
	afterEach,
	beforeEach,
	describe,
	expect,
	expectTypeOf,
	it,
	vi,
} from "vitest";
import { flushPromises, mount, shallowMount } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import { useUserStore } from "@/stores/user.js";
import * as axios from "@/utils/axios.js";
import NewListing from "@/views/NewListing.vue";
import SelectInput from "@/components/Inputs/SelectInput.vue";
import StarsInput from "@/components/Inputs/StarsInput.vue";
import ImageInputList from "@/components/Inputs/ImageInputList.vue";
import FormButton from "@/components/Inputs/FormButton.vue";
import NotificationBox from "@/components/NotificationBox.vue";
import TextAreaWithCounter from "@/components/Inputs/TextAreaWithCounter.vue";
import { mockImplementationFactory } from "../helpers";

const makeFormValid = form => {
	Object.keys(form).forEach(field => {
		form[field].valid = true;
	});
};
const hasColorClasses = (classes, color) =>
	classes.filter(cl => cl.includes(color)).length > 0;

describe("NewListing mount", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());

	it("NewListing should mount", () => {
		expect(NewListing).toBeTruthy();
		expect(wrapper.exists()).toBe(true);
	});

	it("NewListing should call the useNotification composable", () => {
		expect(wrapper.vm.displayMsg).toBeTruthy();
	});
});

describe("NewListing HTML elements", () => {
	const wrapper = shallowMount(NewListing, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
		},
	});

	it("should have a main", () => {
		expect(wrapper.find("main").exists()).toBe(true);
	});

	it("should have a single h1", () => {
		expect(wrapper.findAll("h1").length).toBe(1);
	});

	it("should have a form", () => {
		expect(wrapper.find("form").exists()).toBe(true);
	});

	it("should have three inputs", () => {
		expect(wrapper.findAll("input").length).toBe(3);
	});

	wrapper.unmount();
});

describe("NewListing components", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());

	it("should have a TextAreaWithCounter", () => {
		expect(wrapper.findComponent(TextAreaWithCounter).exists()).toBe(true);
	});

	it("should have a SelectInput", () => {
		expect(wrapper.findComponent(SelectInput).exists()).toBe(true);
	});

	it("should have a StarsInput", () => {
		expect(wrapper.findComponent(StarsInput).exists()).toBe(true);
	});

	it("should have a ImageInputList", () => {
		expect(wrapper.findComponent(ImageInputList).exists()).toBe(true);
	});

	it("should have a FormButton", () => {
		expect(wrapper.findComponent(FormButton).exists()).toBe(true);
	});

	it("should have a NotificationBox", () => {
		expect(wrapper.findComponent(NotificationBox).exists()).toBe(true);
	});

	it("the NotificationBox should not be visible", () => {
		expect(wrapper.findComponent(NotificationBox).isVisible()).toBe(false);
	});
});

describe("NewListing handleFormSubmit", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());

	const mockResponse = {
		response: { data: { id: 1 } },
	};

	it("should display a notification when sent an invalid form", async () => {
		expect(wrapper.vm.isValidForm(wrapper.vm.formData)).toBe(false);

		wrapper.vm.handleFormSubmit();
		await flushPromises();

		const notification = wrapper.findComponent(NotificationBox);
		expect(notification.isVisible()).toBe(true);

		const notificationHasError = hasColorClasses(
			notification.classes(),
			"red"
		);
		expect(notificationHasError).toBe(true);
	});

	it("should display a red notification when postNewListing fails", async () => {
		vi.spyOn(axios, "postNewListing").mockImplementationOnce(() => ({
			error: "foo",
			msg: "bar",
		}));

		makeFormValid(wrapper.vm.formData);
		wrapper.vm.handleFormSubmit();
		await flushPromises();

		const notification = wrapper.findComponent(NotificationBox);
		expect(notification.isVisible()).toBe(true);

		const notificationHasError = hasColorClasses(
			notification.classes(),
			"red"
		);
		expect(notificationHasError).toBe(true);
	});

	it("should display a red notification when postImagesOfListing fails", async () => {
		vi.spyOn(axios, "postNewListing").mockImplementationOnce(
			() => mockResponse
		);
		vi.spyOn(axios, "postImagesOfListing").mockImplementationOnce(() => ({
			error: true,
			msg: "foo",
		}));

		makeFormValid(wrapper.vm.formData);
		wrapper.vm.handleFormSubmit();
		await flushPromises();

		const notification = wrapper.findComponent(NotificationBox);
		expect(notification.isVisible()).toBe(false);
	});

	it("should display a green notification when nothing fails", async () => {
		vi.spyOn(axios, "postNewListing").mockImplementationOnce(
			() => mockResponse
		);
		vi.spyOn(axios, "postImagesOfListing").mockImplementationOnce(
			() => ({})
		);

		makeFormValid(wrapper.vm.formData);
		wrapper.vm.handleFormSubmit();
		await flushPromises();

		const notification = wrapper.findComponent(NotificationBox);
		expect(notification.isVisible()).toBe(true);

		const isSuccessNotification = hasColorClasses(
			notification.classes(),
			"green"
		);
		expect(isSuccessNotification).toBe(true);
	});
});

describe("NewListing organizePostPetition", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());

	it("should return and object with the same data as formData", () => {
		let postData = wrapper.vm.organizePostPetition();

		expect(postData.name).toBe(wrapper.vm.formData.name.value);
		expect(postData.detail).toBe(wrapper.vm.formData.detail.value);
		expect(postData.condition).toBe(wrapper.vm.formData.condition.value);
		expect(postData.price).toBe(wrapper.vm.formData.price.value);
		expect(postData.stock).toBe(wrapper.vm.formData.stock.value);

		wrapper.vm.formData.name.value = "foo";
		wrapper.vm.formData.detail.value = "bar";
		wrapper.vm.formData.condition.value = -1;
		wrapper.vm.formData.price.value = 100000000;
		wrapper.vm.formData.stock.value = 1;

		postData = wrapper.vm.organizePostPetition();
		expect(postData.name).toBe(wrapper.vm.formData.name.value);
		expect(postData.detail).toBe(wrapper.vm.formData.detail.value);
		expect(postData.condition).toBe(wrapper.vm.formData.condition.value);
		expect(postData.price).toBe(wrapper.vm.formData.price.value);
		expect(postData.stock).toBe(wrapper.vm.formData.stock.value);
	});

	it("should return and object that includes the user id from the store", () => {
		let postData;
		const user = useUserStore();

		user.id = 1;
		postData = wrapper.vm.organizePostPetition();
		expect(postData["seller_id"]).toBe(user.id);

		user.id = 100000;
		postData = wrapper.vm.organizePostPetition();
		expect(postData["seller_id"]).toBe(user.id);

		user.id = -1;
		postData = wrapper.vm.organizePostPetition();
		expect(postData["seller_id"]).toBe(user.id);
	});
});

describe("NewListing createNewListing", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());

	it("should call postNewListing function", () => {
		const functionSpy = vi
			.spyOn(axios, "postNewListing")
			.mockImplementationOnce(data => ({ error: "foo", msg: "bar" }));

		wrapper.vm.createNewListing({});
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an object with an error when postNewListing returns an error", async () => {
		vi.spyOn(axios, "postNewListing").mockImplementationOnce(data => ({
			error: "foo",
			msg: "bar",
		}));

		const results = await wrapper.vm.createNewListing({});
		expect(results).toHaveProperty("error");
		expect(results.error).toBe("foo");
		expect(results).toHaveProperty("msg");
		expect(results.msg).toBe("bar");
	});

	it("should return an object with newListingId when postNewListing succeds", async () => {
		const mockResponse = {
			response: { data: { id: 1 } },
		};
		vi.spyOn(axios, "postNewListing").mockImplementationOnce(
			data => mockResponse
		);

		const results = await wrapper.vm.createNewListing({});
		expect(results).toHaveProperty("newListingId");
		expect(results.newListingId).toBe(mockResponse.response.data.id);
	});

	it("should throw an error if postNewListing doesn't return a response", () => {
		vi.spyOn(axios, "postNewListing").mockImplementationOnce(data => {});

		expect(async () => {
			await wrapper.vm.createNewListing({});
		}).rejects.toThrowError();
	});
});

describe("NewListing postImages", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());

	it("should call postImagesOfListing function", async () => {
		const functionSpy = vi
			.spyOn(axios, "postImagesOfListing")
			.mockImplementationOnce(() => ({}));

		await wrapper.vm.postImages(0);
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an empty object on success ", async () => {
		vi.spyOn(axios, "postImagesOfListing").mockImplementationOnce(() => ({
			foo: "",
		}));

		const results = await wrapper.vm.postImages(0);
		expectTypeOf(results).toBeObject();
	});

	it("should call deleteListing function when server returns an error", async () => {
		vi.spyOn(axios, "postImagesOfListing").mockImplementationOnce(() => ({
			error: true,
		}));

		const functionSpy = vi
			.spyOn(axios, "deleteListing")
			.mockImplementationOnce(() => ({}));

		await wrapper.vm.postImages(0);
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an object with an error and a msg when postImagesOfListing returns an error", async () => {
		vi.spyOn(axios, "postImagesOfListing").mockImplementationOnce(() => ({
			error: "foo",
			msg: "bar",
		}));
		vi.spyOn(axios, "deleteListing").mockImplementationOnce(() => ({}));

		const results = await wrapper.vm.postImages(0);
		expect(results).toHaveProperty("error");
		expect(results.error).toBe("foo");
		expect(results).toHaveProperty("msg");
		expect(results.msg).toBe("bar");
	});
});

describe("NewListing fetchCategories", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());

	it("should call getAllCategories function", () => {
		const functionSpy = vi
			.spyOn(axios, "getAllCategories")
			.mockImplementationOnce(mockImplementationFactory("foo", "bar"));

		wrapper.vm.fetchCategories();
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should populate the categories", async () => {
		const mockCategories = [
			{ name: "Cat1", active: true },
			{ name: "Cat2", active: true },
			{ name: "Cat3", active: true },
		];
		vi.spyOn(axios, "getAllCategories").mockImplementationOnce(
			mockImplementationFactory("data", mockCategories)
		);

		expect(wrapper.vm.categories.length).toBe(0);
		wrapper.vm.fetchCategories();
		await flushPromises();
		expect(wrapper.vm.categories.length).toBe(mockCategories.length);
	});

	it("should include only active categories", async () => {
		const mockCategories = [
			{ name: "Cat1", active: false },
			{ name: "Cat2", active: true },
			{ name: "Cat3", active: false },
		];
		const activeCategories = mockCategories.filter(cat => cat.active);
		vi.spyOn(axios, "getAllCategories").mockImplementationOnce(
			mockImplementationFactory("data", mockCategories)
		);

		expect(wrapper.vm.categories.length).toBe(0);
		wrapper.vm.fetchCategories();
		await flushPromises();
		expect(wrapper.vm.categories.length).toBe(activeCategories.length);
	});
});

describe("Newlisting validateTextField", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());
	const mockEvent = { target: { id: "name", value: "foo" } };

	it("should change the valid property of a valid input in formData", () => {
		wrapper.vm.formData.name.min = 4;
		wrapper.vm.formData.name.valid = true;

		wrapper.vm.validateTextField(mockEvent);
		expect(wrapper.vm.formData.name.valid).toBe(false);

		wrapper.vm.formData.name.max = 5;
		wrapper.vm.formData.name.valid = true;
		mockEvent.target.value = "123456";

		wrapper.vm.validateTextField(mockEvent);
		expect(wrapper.vm.formData.name.valid).toBe(false);
	});

	it("should change the valid property of an invalid input in formData", () => {
		wrapper.vm.formData.name.min = 1;

		wrapper.vm.validateTextField(mockEvent);
		expect(wrapper.vm.formData.name.valid).toBe(true);
	});

	it("should display a notification if the input is not valid", async () => {
		const wrapper = mount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});

		wrapper.vm.formData.name.max = 2;
		wrapper.vm.validateTextField(mockEvent);

		await wrapper.vm.$nextTick();
		const notification = wrapper.findComponent(NotificationBox);
		expect(notification.isVisible()).toBe(true);
		wrapper.unmount();
	});
});

describe("Newlisting validateNumericField", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());
	const mockEvent = { target: { id: "price", value: "1" } };

	it("should change the valid property of a valid input in formData", () => {
		wrapper.vm.formData.price.min = 4;
		wrapper.vm.formData.price.valid = true;

		wrapper.vm.validateTextField(mockEvent);
		expect(wrapper.vm.formData.price.valid).toBe(false);

		wrapper.vm.formData.price.max = 5;
		wrapper.vm.formData.price.valid = true;
		mockEvent.target.value = "123456";

		wrapper.vm.validateTextField(mockEvent);
		expect(wrapper.vm.formData.price.valid).toBe(false);
	});

	it("should change the valid property of an invalid input in formData", () => {
		wrapper.vm.formData.price.min = 1;

		wrapper.vm.validateTextField(mockEvent);
		expect(wrapper.vm.formData.price.valid).toBe(true);
	});

	it("should display a notification if the input is not valid", async () => {
		const wrapper = mount(NewListing, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});

		mockEvent.target.value = "1";
		wrapper.vm.formData.price.min = 2;
		wrapper.vm.validateTextField(mockEvent);

		await wrapper.vm.$nextTick();
		const notification = wrapper.findComponent(NotificationBox);
		expect(notification.isVisible()).toBe(true);
		wrapper.unmount();
	});
});
