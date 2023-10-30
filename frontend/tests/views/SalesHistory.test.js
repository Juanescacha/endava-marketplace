import {
	afterEach,
	beforeEach,
	describe,
	expect,
	expectTypeOf,
	it,
	vi,
} from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import SalesHistory from "@/views/SalesHistory.vue";
import { useUserStore } from "@/stores/user.js";
import * as axios from "@/utils/axios.js";

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

let wrapper;
beforeEach(() => {
	wrapper = shallowMount(SalesHistory, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
		},
	});
});
afterEach(() => wrapper.unmount());

describe("SalesHistory mount", () => {
	it("should mount", () => {
		expect(wrapper).toBeTruthy();
	});

	// it("should call a method to fetch data", () => {
	// 	const spy = spyOn(wrapper.vm, "getPageData");
	// 	expect(spy).toBeCalled();
	// });

	it("should have soldProducts set", () => {
		wrapper.vm.soldProducts = mockData;
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
	wrapper.unmount();
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

describe("SalesHistory getPageData", () => {
	it("should call a function to fetch the data from the server", async () => {
		const functionSpy = vi
			.spyOn(axios, "getSellerSales")
			.mockImplementationOnce(() => ["foo"]);
		await wrapper.vm.getPageData();
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should set soldProducts when data is fetch correctly", async () => {
		vi.spyOn(axios, "getSellerSales").mockImplementationOnce(() => ({
			data: ["foo", "bar"],
		}));
		await wrapper.vm.getPageData();
		expect(wrapper.vm.soldProducts.length).toBe(2);
	});

	it("should not set soldProducts when data is not fetched", async () => {
		vi.spyOn(axios, "getSellerSales").mockImplementationOnce(() => ({}));
		await wrapper.vm.getPageData();
		expectTypeOf(wrapper.vm.soldProducts).toBeArray();
		expect(wrapper.vm.soldProducts.length).toBe(0);
	});

	it("should not set soldProducts when data is not an array", async () => {
		vi.spyOn(axios, "getSellerSales").mockImplementationOnce(() => ({
			data: "Foo",
		}));
		await wrapper.vm.getPageData();
		expectTypeOf(wrapper.vm.soldProducts).toBeArray();
		expect(wrapper.vm.soldProducts.length).toBe(0);
	});
});

describe("SalesHistory handleSaleStatusUpdate", () => {
	it("should open the modal", () => {
		expect(wrapper.vm.modal.open).toBe(false);
		wrapper.vm.handleSaleStatusUpdate(1, "cancel");
		expect(wrapper.vm.modal.open).toBe(true);
	});

	it("should update the 'update' object", () => {
		wrapper.vm.handleSaleStatusUpdate(1, "cancel");
		expect(wrapper.vm.update.intention).toBe("cancel");
		expect(wrapper.vm.update.onProduct).toBe(1);

		wrapper.vm.handleSaleStatusUpdate(100000000, "foo");
		expect(wrapper.vm.update.intention).toBe("foo");
		expect(wrapper.vm.update.onProduct).toBe(100000000);
	});
});

describe("SalesHistory handleModalClose", () => {
	it("should close the modal", () => {
		wrapper.vm.handleSaleStatusUpdate();
		expect(wrapper.vm.modal.open).toBe(true);
		wrapper.vm.handleModalClose();
		expect(wrapper.vm.modal.open).toBe(false);
	});

	it("should reset the 'update' object", () => {
		expect(wrapper.vm.update.intention).toBe("");
		expect(wrapper.vm.update.onProduct).toBe(0);

		wrapper.vm.handleSaleStatusUpdate(1, "cancel");
		wrapper.vm.handleModalClose();

		expect(wrapper.vm.update.intention).toBe("");
		expect(wrapper.vm.update.onProduct).toBe(0);
	});
});

describe("SalesHistory updateSaleStatus", () => {
	it("should update the items being processed", () => {
		expect(wrapper.vm.itemsBeingProcessed.length).toBe(0);
		wrapper.vm.updateSaleStatus();
		expect(wrapper.vm.itemsBeingProcessed.length).toBe(1);
	});

	it("should call a function to patch the sale", () => {
		const patchSpy = vi
			.spyOn(axios, "patchSaleStatus")
			.mockImplementationOnce(() => ({}));
		wrapper.vm.updateSaleStatus();
		expect(patchSpy).toHaveBeenCalled();
	});

	it("should restore items being processed at the end", async () => {
		wrapper.vm.handleSaleStatusUpdate(2, "cancel");
		await wrapper.vm.updateSaleStatus();
		expect(wrapper.vm.itemsBeingProcessed.includes(2)).toBe(false);
	});

	it("should restore items being processed even if the patch failed", async () => {
		vi.spyOn(axios, "patchSaleStatus").mockImplementationOnce(() => ({
			error: true,
		}));
		wrapper.vm.handleSaleStatusUpdate(2, "cancel");
		await wrapper.vm.updateSaleStatus();
		expect(wrapper.vm.itemsBeingProcessed.includes(2)).toBe(false);
	});

	it("should close the modal and restore the update object at the end", async () => {
		wrapper.vm.handleSaleStatusUpdate(2, "cancel");
		await wrapper.vm.updateSaleStatus();
		expect(wrapper.vm.modal.open).toBe(false);
		expect(wrapper.vm.update.onProduct).toBe(0);
		expect(wrapper.vm.update.intention).toBe("");
	});
});
