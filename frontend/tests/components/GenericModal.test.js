import { describe, expect, it } from "vitest";
import { shallowMount } from "@vue/test-utils";
import GenericModal from "@/components/GenericModal.vue";

const minProps = {
	title: "Foo",
};

const props = {
	title: "Foo",
	description: "My modal",
	open: true,
};

describe("GenericModal mounts with default props", () => {
	const wrapper = shallowMount(GenericModal, { props: minProps });

	it("should mount", () => {
		expect(GenericModal).toBeTruthy();
	});

	it("should mount with the default 'description' prop", () => {
		expect(wrapper.props("description")).toBeUndefined();
	});

	it("should mount with the default 'open' prop", () => {
		expect(wrapper.props("open")).toBe(false);
	});
	wrapper.unmount();
});

describe("GenericModal mount with provided props", () => {
	const wrapper = shallowMount(GenericModal, { props });

	it("should mount with the title prop", () => {
		expect(wrapper.props("title")).toBe(props.title);
	});

	it("should mount with the description prop", () => {
		expect(wrapper.props("description")).toBe(props.description);
	});

	it("should mount with the open prop", () => {
		expect(wrapper.props("open")).toBe(props.open);
	});
	wrapper.unmount();
});

/* TODO test the conditional rendering inside headlessui Dialog component */
