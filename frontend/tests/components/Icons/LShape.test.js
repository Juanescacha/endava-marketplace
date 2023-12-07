import { afterEach, beforeEach, describe, expect, it, test } from "vitest";
import { mount } from "@vue/test-utils";
import LShape from "@/components/Icons/LShape.vue";

const props = {
	size: 30,
};

describe("LShape mounts with default value props", () => {
	const wrapper = mount(LShape);

	it("should mount", () => {
		expect(LShape).toBeTruthy();
		expect(wrapper.html()).toBeTruthy();
	});

	it("should mount with the default 'size' prop", () => {
		expect(wrapper.props("size")).toBe(15);
	});
	wrapper.unmount();
});

describe("LShape mount with provided values", () => {
	const wrapper = mount(LShape, { props });

	it("should mount with the size prop", () => {
		expect(wrapper.props("size")).toBe(props.size);
	});

	wrapper.unmount();
});

describe("LShape prop validation", () => {
	const sizeValidator = LShape.props.size.validator;

	test("size validator should return true on positive numbers", () => {
		expect(sizeValidator(1)).toBe(true);
		expect(sizeValidator(0.01)).toBe(true);
		expect(sizeValidator(100000)).toBe(true);
	});

	test("size validator should return false on 0 and negative numbers", () => {
		expect(sizeValidator(-0.001)).toBe(false);
		expect(sizeValidator(-1)).toBe(false);
		expect(sizeValidator(-100000)).toBe(false);
	});

	test("size validator should handle strings", () => {
		expect(sizeValidator("foo")).toBe(false);
		expect(sizeValidator("1foo")).toBe(false);
		expect(sizeValidator("1")).toBe(true);
		expect(sizeValidator("-1")).toBe(false);
	});
});

describe("LShape HTML tags", () => {
	const wrapper = mount(LShape, { props });

	const svg = wrapper.find("svg");
	const lines = wrapper.findAll("line");

	it("should have a svg tag", () => {
		expect(svg.exists()).toBe(true);
	});

	it("the svg should have a width and height", () => {
		expect(svg.attributes()).toHaveProperty("width");
		expect(svg.attributes()).toHaveProperty("height");
	});

	it("should have a path tag", () => {
		expect(lines.length).toBe(2);
	});
	wrapper.unmount();
});
