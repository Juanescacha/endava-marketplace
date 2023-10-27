import { describe, expect, it, vi } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import UserProfile from "@/views/UserProfile.vue";

const thereIsAdminButton = buttons =>
	buttons.find(btn => btn.html().includes("Admin"));

it("UserProfile should mount", () => {
	const wrapper = shallowMount(UserProfile);
	expect(UserProfile).toBeTruthy();
	expect(wrapper.exists()).toBe(true);
	wrapper.unmount();
});

describe("UserProfile HTML elements", () => {
	const wrapper = shallowMount(UserProfile, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
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
				createTestingPinia({
					createSpy: vi.fn(),
					initialState: {
						user: {
							id: 1,
							name: "John",
							email: "Doe@email.com",
							isAdmin: true,
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

	it("should include a button that redirects to Admin panel if the user is admin", () => {
		const buttons = wrapper.findAll("button");
		expect(thereIsAdminButton(buttons)).toBeTruthy();
	});
	wrapper.unmount();
});
