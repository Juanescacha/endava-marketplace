import { afterEach, beforeEach, describe, expect, it, test } from "vitest";
import { mount } from "@vue/test-utils";
import TextInput from "@/components/Inputs/TextInput.vue";

const props = {
	name: "u-password",
	type: "password",
	rules: { min: 5, required: true },
	placeholder: "Password",
	containerClass: "mx-2 p-2",
};

describe("TextInput mount with default values", () => {
	const wrapper = mount(TextInput, {
		props: {
			name: "foo",
		},
	});

	it("should mount", () => {
		expect(TextInput).toBeTruthy();
	});

	it("should mount with the default 'type' prop", () => {
		expect(wrapper.props("type")).toBe("text");
	});

	it("should mount with the default 'rules' prop", () => {
		expect(wrapper.props("rules")).toBeTypeOf("object");
		expect(wrapper.props("rules").min).toBeTypeOf("number");
		expect(wrapper.props("rules").required).toBeTypeOf("boolean");
	});

	it("should mount with the default 'containerClass' prop", () => {
		expect(wrapper.props("containerClass")).toBe("");
	});
	wrapper.unmount();
});

describe("TextInput mount with provided values", () => {
	const wrapper = mount(TextInput, { props });

	it("should mount with the name prop", () => {
		expect(wrapper.props("name")).toBe("u-password");
	});

	it("should mount with the type prop", () => {
		expect(wrapper.props("type")).toBe("password");
	});

	it("should mount with the rules prop", () => {
		expect(wrapper.props("rules")).toBeTypeOf("object");
		expect(wrapper.props("rules").min).toBe(5);
		expect(wrapper.props("rules").required).toBe(true);
	});

	it("should mount with the type placeholder", () => {
		expect(wrapper.props("placeholder")).toBe("Password");
	});

	it("should mount with the type containerClass", () => {
		expect(wrapper.props("containerClass")).toBe("mx-2 p-2");
	});
	wrapper.unmount();
});

describe("TextInput HTML tags", () => {
	const wrapper = mount(TextInput, { props });

	const input = wrapper.find("input");
	const label = wrapper.find("label");

	it("should have an input tag", () => {
		expect(input.exists()).toBe(true);
	});

	it("the input should have appropiate attributes", () => {
		const { id, type, placeholder } = input.attributes();
		expect(id).toBe(wrapper.props("name"));
		expect(type).toBe(wrapper.props("type"));
		expect(placeholder).toBe(wrapper.props("placeholder"));
	});

	it("should have an label tag", () => {
		expect(label.exists()).toBe(true);
	});

	it("the label should be for the input", () => {
		expect(label.attributes("for")).toBe(wrapper.props("name"));
	});

	it("the label should not ve visible", () => {
		expect(label.classes()).contain("hidden");
	});
	wrapper.unmount();
});

describe("TextInput events", () => {
	let wrapper;
	let input;
	beforeEach(() => {
		wrapper = mount(TextInput, { props });
		input = wrapper.find("input");
	});

	afterEach(() => wrapper.unmount());

	test("focus-out with invalid input", async () => {
		await input.trigger("focusout");
		expect(wrapper.vm.error).toBeTypeOf("string");
	});

	test("focus-out with valid input", async () => {
		await input.setValue("foobar");
		await input.trigger("focusout");
		expect(wrapper.vm.error).toBeNull();
	});

	test("input change emits inputChanged event", async () => {
		await input.trigger("input");
		const emittedEvents = wrapper.emitted();
		expect(emittedEvents).toHaveProperty("inputChanged");
	});

	test("inputChanged event has a payload with id, value, and valid", async () => {
		await input.setValue("foobar");
		await input.trigger("input");
		const payload = wrapper.emitted().inputChanged[0][0];

		expect(payload.id).toBe(wrapper.props("name"));
		expect(payload.value).toBe(input.element.value);
		expect(payload.valid).toBe(true);
	});
});
