import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import { createRouter, createWebHistory } from "vue-router";
import { routes } from "@/router";
import AdminPanel from "@/views/AdminPanel.vue";
import LinkListItem from "@/components/Menus/LinkListItem.vue";

const getRouterInstance = () =>
	createRouter({
		history: createWebHistory(),
		routes,
	});

describe("AdminPanel mount", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(AdminPanel, {
			global: { plugins: [getRouterInstance()] },
		});
	});
	afterEach(() => wrapper.unmount());

	it("AdminPanel should mount", () => {
		expect(AdminPanel).toBeTruthy();
		expect(wrapper.exists()).toBe(true);
	});

	it("AdminPanel should call the useAdminUser composable", () => {
		expect(wrapper.vm.userIsAdmin).toBe(false);
		expect(wrapper.vm.setUserIsAdmin).toBeTruthy();
	});
});

describe("AdminPanel HTML elements", () => {
	const wrapper = shallowMount(AdminPanel, {
		global: { plugins: [getRouterInstance()] },
	});

	it("should have a main element", () => {
		expect(wrapper.find("main").exists()).toBe(true);
	});

	it("should have a nav element", () => {
		expect(wrapper.find("nav").exists()).toBe(true);
	});

	it("should have an ul element", () => {
		expect(wrapper.find("ul").exists()).toBe(true);
	});

	it("should have multiple li elements", () => {
		const lis = wrapper.findAll("li");
		expect(lis.length).toBeGreaterThan(1);
	});

	it("should have a router-view", () => {
		expect(wrapper.find("router-view-stub").exists()).toBe(true);
	});
	wrapper.unmount();
});

describe("AdminPanel components", () => {
	const wrapper = mount(AdminPanel, {
		global: { plugins: [getRouterInstance()] },
	});

	const lis = wrapper.findAll("li");
	it("should have LinkListItem components", () => {
		const linkListItems = wrapper.findAllComponents(LinkListItem);
		expect(linkListItems.length).toBe(lis.length);
	});
	wrapper.unmount();
});
