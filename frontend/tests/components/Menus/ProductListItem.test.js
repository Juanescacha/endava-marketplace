import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";
import ProductListItem from "@/components/Menus/ProductListItem.vue";

describe("ProductListItem", () => {
	const wrapper = mount(ProductListItem, {
		slots: {
			"left-side": "<section>Foo</section>",
			"right-side": "<aside>Bar</aside>",
		},
	});

	it("should mount", () => {
		expect(ProductListItem).toBeTruthy();
	});

	it("should have a section tag passed by slots", () => {
		expect(wrapper.find("section").exists()).toBe(true);
	});

	it("should have an aside tag passed by slots", () => {
		expect(wrapper.find("aside").exists()).toBe(true);
	});

	it("should have the contents of section and aside", () => {
		expect(wrapper.html()).toContain("Foo");
		expect(wrapper.html()).toContain("Bar");
	});
	wrapper.unmount();
});
