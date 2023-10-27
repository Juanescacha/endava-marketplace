import {
	afterEach,
	beforeEach,
	describe,
	expect,
	expectTypeOf,
	it,
} from "vitest";
import { mount } from "@vue/test-utils";
import SelectInput from "@/components/Inputs/SelectInput.vue";
import { capitalizeFirstLetter } from "@/utils/strings.js";

const props = {
	id: "foo",
	title: "Choose an option",
	options: [
		{ id: 0, name: "foo" },
		{ id: 1, name: "bar" },
	],
	dynamicClass: "flex mt-2",
};

describe("SelectInput mounts with props", () => {
	const wrapper = mount(SelectInput, { props });

	it("should mount", () => {
		expect(SelectInput).toBeTruthy();
	});

	it("should mount with the provided 'id' prop", () => {
		expect(wrapper.props("id")).toBe(props.id);
	});

	it("should mount with the provided 'title' prop", () => {
		expect(wrapper.props("title")).toBe(props.title);
	});

	it("should mount with the provided 'options' prop", () => {
		expectTypeOf(wrapper.props("options")).toBeArray();
	});

	it("should mount with the provided 'dynamicClass' prop", () => {
		expect(wrapper.props("dynamicClass")).toBe(props.dynamicClass);
	});
	wrapper.unmount();
});

describe("SelectInput HTML tags", () => {
	const wrapper = mount(SelectInput, { props });

	const select = wrapper.find("select");
	const options = wrapper.findAll("option");

	it("should have a select tag", () => {
		expect(select.exists()).toBe(true);
	});

	it("the select should have the id provided in the props", () => {
		expect(select.attributes().id).toBe(wrapper.props("id"));
	});

	it("the select dhould have the title provided in the props", () => {
		expect(select.attributes().title).toBe(wrapper.props("title"));
	});

	it("the select should have the classes provided in props", () => {
		const classes = props.dynamicClass.split(" ");
		expect(select.classes()).toContain(classes[0]);
		expect(select.classes()).toContain(classes[1]);
	});

	it("should have 3 options elements when provided 2 items", () => {
		expect(options.length).toBe(3);
	});

	it("the first option should be equal to the title prop", () => {
		expect(options[0].element.value).toBe(wrapper.props("title"));
	});

	it("the first option should be disabled", () => {
		expect(options[0].attributes()).toHaveProperty("disabled");
	});

	it("the other options' value should equal the prop", () => {
		const option1Prop = wrapper.props("options")[0].name;
		const option2Prop = wrapper.props("options")[1].name;
		expect(options[1].element.value).toBe(option1Prop);
		expect(options[2].element.value).toBe(option2Prop);
	});

	it("the other options'should have a data-id attribute", () => {
		const option1Prop = wrapper.props("options")[0].id.toString();
		const option2Prop = wrapper.props("options")[1].id.toString();
		expect(options[1].element.dataset.id).toBe(option1Prop);
		expect(options[2].element.dataset.id).toBe(option2Prop);
	});

	it("the other options' text should be capitalized version of the prop", () => {
		const option1Prop = wrapper.props("options")[0].name;
		const option2Prop = wrapper.props("options")[1].name;
		expect(options[1].text()).toBe(capitalizeFirstLetter(option1Prop));
		expect(options[2].text()).toBe(capitalizeFirstLetter(option2Prop));
	});

	wrapper.unmount();
});

describe("SelectInput events", () => {
	let wrapper = null;
	beforeEach(() => (wrapper = mount(SelectInput, { props })));
	afterEach(() => wrapper.unmount());

	it("should emit selectionChanged when the input is clicked", async () => {
		const select = wrapper.find("select");
		await select.trigger("input");

		const emitted = wrapper.emitted();
		expect(emitted).toHaveProperty("selectionChanged");
	});

	it("should emit selectionChanged with the event payload", async () => {
		const select = wrapper.find("select");
		const optionBar = wrapper.props("options")[1].name;

		select.setValue(optionBar);
		await select.trigger("input");

		const event = wrapper.emitted().selectionChanged;
		const payload = event[0][0];

		expect(payload).toHaveProperty("target");
	});

	it("selectionChanged payload should include selected option", async () => {
		const select = wrapper.find("select");
		const optionBar = wrapper.props("options")[1].name;

		select.setValue(optionBar);
		await select.trigger("input");

		const event = wrapper.emitted().selectionChanged;
		const payload = event[0][0];

		expect(payload.target).toHaveProperty("options");
		expect(payload.target).toHaveProperty("selectedIndex");

		const selectedOption =
			payload.target.options[payload.target.selectedIndex].dataset.id;

		expect(selectedOption).toBe("1");
	});
});
