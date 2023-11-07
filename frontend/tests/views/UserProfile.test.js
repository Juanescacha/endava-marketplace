import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import { createRouter, createWebHistory } from "vue-router";
import { createTestingPinia } from "@pinia/testing";
import UserProfile from "@/views/UserProfile.vue";
import { routes } from "@/router";

const thereIsAdminButton = buttons =>
	buttons.find(btn => btn.html().includes("Admin"));

const getRouterInstance = () =>
	createRouter({
		history: createWebHistory(),
		routes,
	});

describe("UserProfile mount", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(UserProfile, {
			global: { plugins: [getRouterInstance()] },
		});
	});
	afterEach(() => wrapper.unmount());

	it("UserProfile should mount", () => {
		expect(UserProfile).toBeTruthy();
		expect(wrapper.exists()).toBe(true);
	});

	it("UserProfile should call the useAdminUser composable", () => {
		expect(wrapper.vm.userIsAdmin).toBe(false);
		expect(wrapper.vm.setUserIsAdmin).toBeTruthy();
	});
});

describe("UserProfile HTML elements", () => {
	const wrapper = shallowMount(UserProfile, {
		global: {
			plugins: [
				getRouterInstance(),
				createTestingPinia({ createSpy: vi.fn() }),
			],
		},
	});

	it("should have an img element", () => {
		expect(wrapper.find("img").exists()).toBe(true);
	});

	it("should have an h1 element", () => {
		expect(wrapper.find("h1").exists()).toBe(true);
	});

	it("should not have more than a h1 element", () => {
		expect(wrapper.findAll("h1").length).toBe(1);
	});

	it("should not have an admin panel button whe the user is not Admin", () => {
		const buttons = wrapper.findAll("button");
		expect(thereIsAdminButton(buttons)).toBeFalsy();
	});
	wrapper.unmount();
});

describe("UserProfile contents", () => {
	const wrapper = mount(UserProfile, {
		global: {
			plugins: [
				getRouterInstance(),
				createTestingPinia({
					createSpy: vi.fn(),
					initialState: {
						user: {
							id: 1,
							name: "John",
							email: "Doe@email.com",
						},
					},
				}),
			],
		},
	});

	it("should include the user's name in the heading", () => {
		expect(wrapper.find("h1").html()).toContain(wrapper.vm.user.name);
	});

	it("should include the user email", () => {
		expect(wrapper.html()).toContain(wrapper.vm.user.email);
	});
	wrapper.unmount();
});
