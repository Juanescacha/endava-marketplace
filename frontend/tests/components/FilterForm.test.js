import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";
import FilterForm from "@/components/FilterForm.vue";
import TextInput from "@/components/Inputs/TextInput.vue";
import FormButton from "@/components/Inputs/FormButton.vue";

let wrapper;
beforeEach(() => (wrapper = mount(FilterForm)));
afterEach(() => wrapper.unmount());

it("FilterForm should mount", () => {
	expect(FilterForm).toBeTruthy();
	expect(wrapper.exists()).toBe(true);
});

describe("FilterForm HTML elements", () => {
	it("FilterForm should have a form tag", () => {
		expect(wrapper.find("form").exists()).toBe(true);
	});

	it("should have a submit button", () => {
		const button = wrapper.find("button");
		expect(button.attributes().type).toBe("submit");
	});
});

describe("FilterForm components", () => {
	it("should have two text inputs", () => {
		const textInputs = wrapper.findAllComponents(TextInput);
		expect(textInputs.length).toBe(2);
	});

	it("should have a form button", () => {
		const formBtn = wrapper.findComponent(FormButton);
		expect(formBtn.exists()).toBe(true);
	});
});

describe("FilterForm events", () => {
	const mockInputChangedName = {
		id: "name",
		value: "foo",
	};
	const mockInputChangedEmail = {
		id: "email",
		value: "bar",
	};

	it("should update formData name and email on input-changed event", async () => {
		const textInputs = wrapper.findAllComponents(TextInput);

		await textInputs[0].vm.emit("inputChanged", mockInputChangedName);
		expect(wrapper.vm.formData.name).toBe(mockInputChangedName.value);

		await textInputs[1].vm.emit("inputChanged", mockInputChangedEmail);
		expect(wrapper.vm.formData.email).toBe(mockInputChangedEmail.value);
	});

	it("should update formData name and email on input changes", async () => {
		const inputs = wrapper.findAll("input");

		inputs[0].setValue(mockInputChangedName.value);
		inputs[1].setValue(mockInputChangedEmail.value);

		await inputs[0].trigger("input");
		await inputs[1].trigger("input");

		expect(wrapper.vm.formData.name).toBe(mockInputChangedName.value);
		expect(wrapper.vm.formData.email).toBe(mockInputChangedEmail.value);
	});

	it("should emit filteredSearch on form submit", async () => {
		const wrapper = mount(FilterForm);
		const form = wrapper.find("form");

		await form.trigger("submit");
		expect(wrapper.emitted()).toHaveProperty("filteredSearch");
	});

	it("should emit filteredSearch with the formData", async () => {
		const wrapper = mount(FilterForm);
		const form = wrapper.find("form");

		await form.trigger("submit");
		const payload = wrapper.emitted().filteredSearch[0][0];
		expect(payload).toHaveProperty("name");
		expect(payload).toHaveProperty("email");
	});

	it("should emited formData should have information from the inputs", async () => {
		const wrapper = mount(FilterForm);
		const form = wrapper.find("form");
		const inputs = wrapper.findAll("input");

		inputs[0].setValue(mockInputChangedName.value);
		inputs[1].setValue(mockInputChangedEmail.value);

		await inputs[0].trigger("input");
		await inputs[1].trigger("input");

		await form.trigger("submit");
		const payload = wrapper.emitted().filteredSearch[0][0];
		expect(payload.name).toBe(mockInputChangedName.value);
		expect(payload.email).toBe(mockInputChangedEmail.value);
	});
});
