import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { mount } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import SalesHistory from "@/views/SalesHistory.vue";
import { useUserStore } from "@/stores/user.js";
import { spyOn } from "@vitest/spy";

const mockData = [
	{
		id: 1,
		date: "2023-10-05",
		listing: {
			name: "Iphone",
		},
		buyer: {
			name: "John Doe",
		},
		status: {
			name: "Pending",
		},
	},
];

describe("SalesHistory mount", () => {
	const wrapper = mount(SalesHistory, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
		},
	});
	wrapper.vm.soldProducts = mockData;

	it("should mount", () => {
		expect(wrapper).toBeTruthy();
	});

	// it("should call a method to fetch data", () => {
	// 	const spy = spyOn(wrapper.vm, "getPageData");
	// 	expect(spy).toBeCalled();
	// });

	it("should have soldProducts set", () => {
		expect(wrapper.vm.soldProducts.length).toBe(1);
	});
});

describe("SalesHistory access to stores", () => {
	const wrapper = mount(SalesHistory, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
		},
	});
	const user = useUserStore();

	it("shouldn't throw error when accessing the user store", () => {
		expect(user).toBeDefined();
	});
});

describe("SalesHistory DOM elements", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(SalesHistory, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	afterEach(() => wrapper.unmount());

	it("should not have a ul element when soldProducts is empty", () => {
		expect(wrapper.find("ul").exists()).toBe(false);
	});

	// it("should have a ul element whe soldProduct has items", () => {
	// 	wrapper.vm.soldProducts = mockData; // Needs integration/mock server in orther to properly set this value
	// 	expect(wrapper.find("ul").exists()).toBe(true);
	// });
});
