import { beforeEach, describe, expect, test } from "vitest";
import { mount } from "@vue/test-utils";
import QuantitySelect from "@/components/Inputs/QuantitySelect.vue";

const props = { stock: 4 };
const optionsSelector = "[data-ui='qty-option']";
const selectSelector = "[data-ui='select-qty']";

test("QuantitySelect mounts", () => {
	const wrapper = mount(QuantitySelect, { props });
	expect(QuantitySelect, "the component exist").toBeTruthy();
	expect(wrapper.exists(), "the component mounts").toBe(true);
});

describe("QuantitySelect option selection", () => {
	let wrapper;
	beforeEach(() => (wrapper = mount(QuantitySelect, { props })));

	test("there should be a number of options equalling the amount in stock", () => {
		const options = wrapper.findAll(optionsSelector);
		expect(options.length).toBe(props.stock);
	});

	test("when clicked an option, an event should be emitted", async () => {
		const selectWrapper = wrapper.find(selectSelector);

		selectWrapper.setValue(1);
		selectWrapper.trigger("input");

		expect(wrapper.emitted()).toHaveProperty("quantitySelected");
	});

	test("the emitted event should pass the selected number", async () => {
		const selectWrapper = wrapper.find(selectSelector);

		selectWrapper.setValue(2);
		selectWrapper.trigger("input");

		const payload = wrapper.emitted().quantitySelected[0][0];

		expect(payload).toBe(2);
	});
});
