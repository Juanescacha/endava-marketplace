import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";
import NotificationBox from "@/components/NotificationBox.vue";

const props = {
	message: "Foo",
	color: "green",
};

describe("NotificationBox mounts with default props", () => {
	const wrapper = mount(NotificationBox);

	it("should mount", () => {
		expect(NotificationBox).toBeTruthy();
	});

	it("should mount with the default 'message' prop", () => {
		expect(wrapper.props("message")).toBeNull();
	});

	it("should mount with the default 'color' prop", () => {
		expect(wrapper.props("color")).toBe("gray");
	});
	it("should not have any contents if no message is provided", () => {
		expect(wrapper.find("div").exists()).toBe(false);
	});
	wrapper.unmount();
});

describe("NotificationBox mounts with provided props", () => {
	const wrapper = mount(NotificationBox, { props });

	it("should mount with the 'message' prop", () => {
		expect(wrapper.props("message")).toBe(props.message);
	});

	it("should mount with the 'color' prop", () => {
		expect(wrapper.props("color")).toBe(props.color);
	});
	wrapper.unmount();
});

describe("NotificationBox classes", () => {
	it("should have green background and text, when provided the green color", () => {
		const wrapper = mount(NotificationBox, { props });
		const element = wrapper.find("div");
		expect(element.classes()).toContain("bg-green-200");
		expect(element.classes()).toContain("text-green-500");
		wrapper.unmount();
	});

	it("should have gray background and text, when no color is provided", () => {
		const wrapper = mount(NotificationBox, { props: { message: "Foo" } });
		const element = wrapper.find("div");
		expect(element.classes()).toContain("bg-gray-200");
		expect(element.classes()).toContain("text-gray-500");
		wrapper.unmount();
	});
});
