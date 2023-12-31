import { describe, expect, it } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import { createRouter, createWebHistory } from "vue-router";
import UserDashboard from "@/views/UserDashboard.vue";
import LinkListItem from "@/components/Menus/LinkListItem.vue";
import { routes } from "@/router";

const getRouterInstance = () =>
	createRouter({
		history: createWebHistory(),
		routes,
	});

it("UserDashboard should mount", () => {
	const wrapper = shallowMount(UserDashboard, {
		global: { plugins: [getRouterInstance()] },
	});
	expect(UserDashboard).toBeTruthy();
	expect(wrapper.exists()).toBe(true);
	wrapper.unmount();
});

describe("UserDashboard HTML elements", () => {
	const wrapper = shallowMount(UserDashboard, {
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

describe("UserDashboard components", () => {
	const wrapper = mount(UserDashboard, {
		global: { plugins: [getRouterInstance()] },
	});

	const lis = wrapper.findAll("li");
	it("should have LinkListItem components", () => {
		const linkListItems = wrapper.findAllComponents(LinkListItem);
		expect(linkListItems.length).toBe(lis.length);
	});

	it("should have svg's coming from heroicons components", () => {
		const svgs = wrapper.findAll("svg");
		expect(svgs.length).toBe(lis.length);
	});
	wrapper.unmount();
});
