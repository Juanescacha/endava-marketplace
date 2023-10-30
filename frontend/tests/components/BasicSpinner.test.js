import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";
import BasicSpinner from "@/components/BasicSpinner.vue";

let wrapper;
beforeEach(() => (wrapper = mount(BasicSpinner)));
afterEach(() => wrapper.unmount());

it("Basic spinner should mount", () => {
	expect(BasicSpinner).toBeTruthy();
	expect(wrapper.exists()).toBe(true);
});

describe("BasicSpinner attributes", () => {
	it("should have an aria-label attribute", () => {
		const div = wrapper.find("div");
		expect(div.attributes()).toHaveProperty("aria-label");
	});

	it("should have an role attribute", () => {
		const div = wrapper.find("div");
		expect(div.attributes()).toHaveProperty("role");
	});
});

it("BasicSpinner should have an animate-spin class", () => {
	const div = wrapper.find("div");
	expect(div.classes()).toContain("animate-spin");
});
