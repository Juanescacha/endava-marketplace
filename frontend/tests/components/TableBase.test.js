import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";
import TableBase from "@/components/TableBase.vue";

const props = { columns: ["Foo", "Bar"] };
let wrapper;
beforeEach(() => (wrapper = mount(TableBase, { props })));
afterEach(() => wrapper.unmount());

it("TableBase should mount", () => {
	expect(TableBase).toBeTruthy();
	expect(wrapper.exists()).toBe(true);
});

describe("TableBase HTML elements", () => {
	it("should have a table element", () => {
		expect(wrapper.find("table").exists()).toBe(true);
	});

	it("should have a thead element", () => {
		expect(wrapper.find("thead").exists()).toBe(true);
	});

	it("should have a tbody element", () => {
		expect(wrapper.find("tbody").exists()).toBe(true);
	});

	it("should have a number of 'th' elements matching the number of columns", () => {
		expect(wrapper.findAll("th").length).toBe(wrapper.vm.columns.length);
	});

	it("should have headers matching the columns", () => {
		const headers = wrapper.findAll("th");
		expect(headers[0].text()).toBe(wrapper.vm.columns[0]);
		expect(headers[1].text()).toBe(wrapper.vm.columns[1]);
	});
});
