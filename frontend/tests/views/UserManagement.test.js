import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import * as axios from "@/utils/axios";
import UserManagement from "@/views/UserManagement.vue";
import { mockImplementationFactory } from "../helpers";

describe("UserManagement mount", () => {
	let wrapper;
	beforeEach(() => (wrapper = shallowMount(UserManagement)));
	afterEach(() => wrapper.unmount());

	it("UserManagement should mount", () => {
		expect(UserManagement).toBeTruthy();
		expect(wrapper.exists()).toBe(true);
	});

	it("UserManagement should call the useUserList composable", () => {
		expect(wrapper.vm.users).toBeTruthy();
		expect(wrapper.vm.currentPage).toBe(1);
		expect(wrapper.vm.fetchUsers).toBeTruthy();
	});
});

describe("UserManagement HTML elements", () => {
	const wrapper = shallowMount(UserManagement);
	const controls = wrapper.find("#paginationControls");

	it("should display the current page", () => {
		expect(controls.html()).toContain(wrapper.vm.currentPage);
	});

	it("should have a previous page button", () => {
		const btn = controls.find("#previousPage");
		expect(btn.exists()).toBe(true);
		expect(btn.attributes().type).toBe("button");
	});

	it("should have a next page button", () => {
		const btn = controls.find("#nextPage");
		expect(btn.exists()).toBe(true);
		expect(btn.attributes().type).toBe("button");
	});
	wrapper.unmount();
});

describe("UserManagement table", () => {
	const wrapper = mount(UserManagement);
	const table = wrapper.find("table");

	it("should have a table element", () => {
		expect(table.exists()).toBe(true);
	});

	it("should have a header with n amount of columns", () => {
		const headers = table.findAll("th");
		expect(table.find("thead").exists()).toBe(true);
		expect(headers.length).toBe(wrapper.vm.columns.length);
	});

	it("should have a body", () => {
		expect(table.find("tbody").exists()).toBe(true);
	});

	wrapper.unmount();
});

describe("UserManagment methods", () => {
	const wrapper = shallowMount(UserManagement);
	const mockEvent = {
		target: { checked: true },
	};
	const mockUser = { id: 1, email: "foo", admin: false };
	const mockFilters = { name: "email", email: "name" };

	it("handleAdminToogle should call patchAdminRole", () => {
		const axiosSpy = vi
			.spyOn(axios, "patchAdminRole")
			.mockImplementationOnce(mockImplementationFactory("result", {}));
		wrapper.vm.handleAdminToogle(mockEvent, mockUser);
		expect(axiosSpy).toHaveBeenCalled();
	});

	it("handleAdminToogle should call patchAdminRole with the user id and a boolean", () => {
		const axiosSpy = vi
			.spyOn(axios, "patchAdminRole")
			.mockImplementationOnce(mockImplementationFactory("result", {}));
		wrapper.vm.handleAdminToogle(mockEvent, mockUser);
		expect(axiosSpy).toHaveBeenCalledWith(
			mockUser.id,
			mockEvent.target.checked
		);
	});

	it("handleAdminToogle should rollback ui changes if operation fails", () => {
		vi.spyOn(axios, "patchAdminRole").mockImplementationOnce(
			mockImplementationFactory("error", true)
		);
		wrapper.vm.handleAdminToogle(mockEvent, mockUser);
		expect(mockUser.admin).toBe(!mockEvent.target.checked);
	});

	it("handleFilteredSearch should change the currentPage value to 1", () => {
		wrapper.vm.currentPage = 2;
		wrapper.vm.handleFilteredSearch(mockFilters);
		expect(wrapper.vm.currentPage).toBe(1);
	});

	wrapper.unmount();
});
