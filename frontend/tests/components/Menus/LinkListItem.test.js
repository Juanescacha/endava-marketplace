import { describe, expect, it } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import { createRouter, createWebHistory } from "vue-router";
import LinkListItem from "@/components/Menus/LinkListItem.vue";
import { routes } from "@/router";

const props = {
	redirectsTo: "/404",
	colorClasses: "text-endava-800 hover:bg-endava-100 active:bg-endava-200",
};

const router = createRouter({
	history: createWebHistory(),
	routes,
});

describe("LinkListItem mounts with default value props", () => {
	const { redirectsTo } = props;
	const wrapper = shallowMount(LinkListItem, {
		props: {
			redirectsTo,
		},
		global: {
			plugins: [router],
		},
	});

	it("should mount", () => {
		expect(LinkListItem).toBeTruthy();
	});

	it("should mount with the default 'colorClasses' prop", () => {
		expect(wrapper.props("colorClasses")).toBe(
			"text-gray-800 hover:bg-gray-100 active:bg-gray-200"
		);
	});
	wrapper.unmount();
});

describe("LinkListItem mounts with provided props", () => {
	const wrapper = shallowMount(LinkListItem, {
		props,
		global: {
			plugins: [router],
		},
	});

	it("should mount with the redirectsTo prop", () => {
		expect(wrapper.props("redirectsTo")).toBe(props.redirectsTo);
	});

	it("should mount with the colorClasses prop", () => {
		expect(wrapper.props("colorClasses")).toBe(props.colorClasses);
	});
	wrapper.unmount();
});

describe("LinkListItem a tags", () => {
	const wrapper = mount(LinkListItem, {
		props,
		global: {
			plugins: [router],
		},
	});
	const a = wrapper.find("a");

	it("should exist", () => {
		expect(a.exists()).toBe(true);
	});

	it("should have the css classes provided in props", () => {
		const classes = props.colorClasses.split(" ");
		classes.forEach(cssClass => {
			expect(a.classes()).toContain(cssClass);
		});
	});

	it("should redirect to the page on redirectsTo prop", async () => {
		await a.trigger("click");
		expect(wrapper.html()).toContain("404");
	});

	wrapper.unmount();
});
