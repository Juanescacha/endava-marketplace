import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import ImageInputList from "@/components/Inputs/ImageInputList.vue";
import ImageInput from "@/components/Inputs/ImageInput.vue";

describe("ImageInputList mounts with props", () => {
	const wrapper = shallowMount(ImageInputList, {
		props: {
			maxAmountOfInputs: 2,
		},
	});

	it("should mount", () => {
		expect(ImageInputList).toBeTruthy();
	});

	it("should mount with the maxAmountOfInputs prop", () => {
		expect(wrapper.props("maxAmountOfInputs")).toBe(2);
	});
	wrapper.unmount();
});

describe("ImageInputList mounts with default props", () => {
	const wrapper = shallowMount(ImageInputList);

	it("should mount", () => {
		expect(ImageInputList).toBeTruthy();
	});

	it("should mount with the maxAmountOfInputs prop", () => {
		expect(wrapper.props("maxAmountOfInputs")).toBe(5);
	});
	wrapper.unmount();
});

describe("ImageInputList updateAmountOfInputs method", () => {
	let wrapper = null;
	let updateAmountOfInputs = null;

	beforeEach(() => {
		wrapper = shallowMount(ImageInputList);
		updateAmountOfInputs = wrapper.vm.updateAmountOfInputs;
	});
	afterEach(() => {
		wrapper.unmount();
		updateAmountOfInputs = null;
	});

	it("should increase amountOfInputs when the param equals the length of media", () => {
		expect(wrapper.vm.amountOfInputs).toBe(1);
		updateAmountOfInputs(0);
		expect(wrapper.vm.amountOfInputs).toBe(2);
	});

	it("should increase by one the length of media", () => {
		expect(wrapper.vm.media.length).toBe(1);
		updateAmountOfInputs(0);
		expect(wrapper.vm.media.length).toBe(2);
	});

	it("should not update amountOfInputs if the param is less than media length", () => {
		updateAmountOfInputs(0);
		expect(wrapper.vm.amountOfInputs).toBe(2);
		updateAmountOfInputs(0);
		expect(wrapper.vm.amountOfInputs).toBe(2);
	});

	it("should not update amountOfInputs if the max number of inputs has been reached", () => {
		const maxAmountOfInputs = wrapper.props("maxAmountOfInputs");
		for (let i = 0; i < maxAmountOfInputs; ++i) {
			updateAmountOfInputs(i);
		}

		expect(wrapper.vm.amountOfInputs).toBe(maxAmountOfInputs);
		updateAmountOfInputs(4);
		expect(wrapper.vm.amountOfInputs).toBe(maxAmountOfInputs);
	});
});

describe("ImageInputList HTML elements", () => {
	let wrapper = null;
	beforeEach(() => (wrapper = mount(ImageInputList)));
	afterEach(() => wrapper.unmount());

	it("should have a ul element", () => {
		expect(wrapper.find("ul").exists()).toBe(true);
	});

	it("should have one li element on mount", () => {
		const lis = wrapper.findAll("li");
		expect(lis.length).toBe(1);
		expect(lis[0].exists()).toBe(true);
	});

	it("should have multiple li element after amount of inputs is updated", async () => {
		await wrapper.vm.updateAmountOfInputs(0);

		const lis = wrapper.findAll("li");
		expect(lis.length).toBe(2);
	});

	it("should have one ImageInput element on mount", () => {
		expect(wrapper.findComponent(ImageInput).exists()).toBe(true);
	});

	it("should have multiple ImageInput element after amount of inputs is updated", async () => {
		await wrapper.vm.updateAmountOfInputs(0);
		const inputs = wrapper.findAllComponents(ImageInput);
		expect(inputs.length).toBe(2);
	});
});

describe("ImageInputList events", () => {
	let wrapper = null;
	beforeEach(() => (wrapper = mount(ImageInputList)));
	afterEach(() => wrapper.unmount());

	const mockEventPayload = {
		idx: 0,
		imageObj: {
			name: "image.png",
			size: 50000,
			type: "image/png",
		},
	};
	const mockEventPayload2 = {
		idx: 0,
		imageObj: {
			name: "newImage.png",
			size: 25000,
			type: "image/png",
		},
	};

	it("should update the empty media list on imageUploaded", async () => {
		const imageInput = wrapper.findComponent(ImageInput);
		await imageInput.vm.emit("imageUploaded", mockEventPayload);

		expect(wrapper.vm.media.length).toBe(2);
		expect(wrapper.vm.media[0]).toHaveProperty("name");
	});

	it("should update any media list item on imageUploaded", async () => {
		const imageInput = wrapper.findComponent(ImageInput);
		await imageInput.vm.emit("imageUploaded", mockEventPayload);

		mockEventPayload.idx = 1;
		await imageInput.vm.emit("imageUploaded", mockEventPayload);

		await imageInput.vm.emit("imageUploaded", mockEventPayload2);

		expect(wrapper.vm.media.length).toBe(3);
		expect(wrapper.vm.media[0].name).toBe("newImage.png");
		expect(wrapper.vm.media[1].name).toBe("image.png");
	});

	it("should emit imageTooLargeUploaded on large file uploaded", async () => {
		const imageInput = wrapper.findComponent(ImageInput);
		await imageInput.vm.emit("imageTooLargeUploaded");
		expect(wrapper.emitted()).toHaveProperty("imageTooLargeUploaded");
	});
});
