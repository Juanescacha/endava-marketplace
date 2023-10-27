import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";
import FormButton from "@/components/Inputs/FormButton.vue";

const props = {
	text: "foo",
};

describe("FormButton mounts with default value props", () => {
	const wrapper = mount(FormButton, { props });

	it("should mount", () => {
		expect(FormButton).toBeTruthy();
	});

	it("should mount with the default 'btnType' prop", () => {
		expect(wrapper.props("btnType")).toBe("button");
	});
	wrapper.unmount();
});

describe("FormButton mount with provided values", () => {
	const wrapper = mount(FormButton, {
		props: {
			text: "foo",
			btnType: "submit",
		},
	});

	it("should mount with the text prop", () => {
		expect(wrapper.props("text")).toBe(props.text);
	});

	it("should mount with the btnType prop", () => {
		expect(wrapper.props("btnType")).toBe("submit");
	});
	wrapper.unmount();
});

describe("FormButton HTML tags", () => {
	const wrapper = mount(FormButton, { props });

	const button = wrapper.find("button");

	it("should have a button tag", () => {
		expect(button.exists()).toBe(true);
	});

	it("the button should be of the appropiate type", () => {
		expect(button.attributes().type).toBe(wrapper.props("btnType"));
	});

	it("the button should have endava style", () => {
		expect(button.classes()).toContain("endava");
	});
	wrapper.unmount();
});
