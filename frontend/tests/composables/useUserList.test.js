import { beforeEach, describe, expect, it, vi } from "vitest";
import { flushPromises } from "@vue/test-utils";
import * as axios from "@/utils/axios";
import useUserList from "@/composables/useUserList";
import { mockImplementationFactory } from "../helpers";

const {
	users,
	currentPage,
	filters,
	fetchUsers,
	updateFilters,
	addToCurrentPageValue,
} = useUserList();

beforeEach(() => {
	users.value = [];
	currentPage.value = 1;
	filters.value = {};
});

describe("fetchUsers function", () => {
	it("should call getUsers function", () => {
		const spy = vi
			.spyOn(axios, "getUsers")
			.mockImplementationOnce(mockImplementationFactory("data", true));

		fetchUsers();
		expect(spy).toHaveBeenCalled();
	});

	it("should call getUsers with the current page and filters", () => {
		const spy = vi
			.spyOn(axios, "getUsers")
			.mockImplementationOnce(
				mockImplementationFactory("data", { content: ["foo", "bar"] })
			);

		fetchUsers();
		expect(spy).toHaveBeenCalledWith(currentPage.value, filters.value);
	});

	it("should update the user list", async () => {
		vi.spyOn(axios, "getUsers").mockImplementationOnce(
			mockImplementationFactory("data", { content: ["foo", "bar"] })
		);

		fetchUsers();
		await flushPromises();
		expect(users.value.length).toBe(2);
		expect(users.value[0]).toBe("foo");
		expect(users.value[1]).toBe("bar");
	});

	it("should not update the user list when the server fails to respond", async () => {
		vi.spyOn(axios, "getUsers").mockImplementationOnce(
			mockImplementationFactory("error", { content: ["foo", "bar"] })
		);

		fetchUsers();
		await flushPromises();
		expect(users.value.length).toBe(0);
	});
});

describe("updateFilters function", () => {
	const mockFilters = { foo: "bar" };

	it("should update filter to the provided object", () => {
		updateFilters(mockFilters);
		expect(filters.value.hasOwnProperty("foo")).toBe(true);
		expect(filters.value.foo).toBe(mockFilters.foo);
	});

	it("should not modify the filters if an inappropiate value is passed", () => {
		updateFilters(mockFilters);

		updateFilters(0);
		expect(filters.value.hasOwnProperty("foo")).toBe(true);

		updateFilters("0");
		expect(filters.value.hasOwnProperty("foo")).toBe(true);

		updateFilters([]);
		expect(filters.value.hasOwnProperty("foo")).toBe(true);

		updateFilters(null);
		expect(filters.value.hasOwnProperty("foo")).toBe(true);
	});

	it("should reset the filters when no arguments are passed", () => {
		updateFilters(mockFilters);

		updateFilters();
		expect(Object.keys(filters.value).length).toBe(0);
	});
});

describe("addToCurrentPageValue function", () => {
	it("should sum the provided value to currentPage", () => {
		expect(currentPage.value).toBe(1);

		addToCurrentPageValue(1);
		expect(currentPage.value).toBe(2);

		addToCurrentPageValue(0);
		expect(currentPage.value).toBe(2);

		addToCurrentPageValue(1000000000);
		expect(currentPage.value).toBe(1000000002);
	});

	it("should substract from currentPage when provided a negative value", () => {
		currentPage.value = 10;

		addToCurrentPageValue(-1);
		expect(currentPage.value).toBe(9);

		addToCurrentPageValue(-2);
		expect(currentPage.value).toBe(7);

		currentPage.value = 100000003;
		addToCurrentPageValue(-100000000);
		expect(currentPage.value).toBe(3);
	});

	it("should not allow the value of currentPage to be below 1", () => {
		expect(currentPage.value).toBe(1);

		addToCurrentPageValue(-1);
		expect(currentPage.value).toBe(1);

		addToCurrentPageValue(-100000000);
		expect(currentPage.value).toBe(1);

		currentPage.value = 5;
		addToCurrentPageValue(-5);
		expect(currentPage.value).toBe(5);
	});
});
