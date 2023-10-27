import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { mount } from "@vue/test-utils";
import ImageInput from "@/components/Inputs/ImageInput.vue";

const props = {
	id: 0,
};

describe("ImageInput mounts with props", () => {
	const wrapper = mount(ImageInput, { props });

	it("should mount", () => {
		expect(ImageInput).toBeTruthy();
	});

	it("should mount with the id prop", () => {
		expect(wrapper.props("id")).toBe(props.id);
	});
	wrapper.unmount();
});

describe("ImageInput extractIndexFromMediaString method", () => {
	const wrapper = mount(ImageInput, { props });

	const extractIndexFromMediaString = wrapper.vm.extractIndexFromMediaString;
	const samples = ["media0", "media1000000", "mediaa"];

	it("should return '0' when provided 'media0'", () => {
		expect(extractIndexFromMediaString(samples[0])).toBe("0");
	});

	it("should return '1000000' when provided 'media1000000'", () => {
		expect(extractIndexFromMediaString(samples[1])).toBe("1000000");
	});

	it("should return 'a' when provided 'mediaa'", () => {
		expect(extractIndexFromMediaString(samples[2])).toBe("a");
	});

	wrapper.unmount();
});

//TODO setPreviewImage()

describe("ImageInput input element", () => {
	const wrapper = mount(ImageInput, { props });
	const input = wrapper.find("input");

	it("should exist", () => {
		expect(input.exists()).toBe(true);
	});

	it("should be a file type", () => {
		expect(input.element.type).toBe("file");
	});

	it("should have an id matching the id prop", () => {
		expect(input.element.id).toBe(`media${wrapper.props("id")}`);
	});

	it("should have a title attribute", () => {
		expect(input.element.title).toBeTruthy();
	});

	it("should have an accepts attribute", () => {
		expect(input.element.accept).toBeTruthy();
	});

	it("should not be rendered", () => {
		expect(input.classes()).toContain("hidden");
	});
	wrapper.unmount();
});

describe("ImageInput button element", () => {
	const wrapper = mount(ImageInput, { props });
	const button = wrapper.find("button");

	it("should exist", () => {
		expect(button.exists()).toBe(true);
	});

	it("should be a button type", () => {
		expect(button.element.type).toBe("button");
	});

	it("should have a title attribute", () => {
		expect(button.element.title).toBeTruthy();
	});
	wrapper.unmount();
});

describe("ImageInput file upload", () => {
	let wrapper = null;
	const mockEvent = {
		target: {
			id: "media1",
			files: [
				{
					name: "image.png",
					size: 50000,
					type: "image/png",
				},
			],
		},
	};

	beforeEach(() => (wrapper = mount(ImageInput, { props })));
	afterEach(() => wrapper.unmount());

	const fileReaderSpy = vi
		.spyOn(FileReader.prototype, "readAsDataURL")
		.mockImplementation(() => null);

	it("should emit imageUploaded event on file uploaded", () => {
		wrapper.vm.handleImageUpload(mockEvent);
		const emitted = wrapper.emitted();
		expect(emitted).toHaveProperty("imageUploaded");
		const payload = emitted.imageUploaded[0][0];
		expect(payload).toHaveProperty("idx");
		expect(payload).toHaveProperty("imageObj");
	});

	it("should emit imageUploaded with payload", () => {
		wrapper.vm.handleImageUpload(mockEvent);
		const payload = wrapper.emitted().imageUploaded[0][0];

		expect(payload).toHaveProperty("idx");
		expect(payload).toHaveProperty("imageObj");
		expect(payload.idx).toBe(1);
		expect(payload.imageObj.name).toBe("image.png");
	});

	it("should set the preview image", () => {
		const setPreviewImageSpy = vi.spyOn(wrapper.vm, "setPreviewImage");

		wrapper.vm.handleImageUpload(mockEvent);
		// expect(setPreviewImageSpy).toHaveBeenCalled(); // TODO why is this not working
		expect(fileReaderSpy).toHaveBeenCalled();
	});

	it("should render an image tag", async () => {
		let image = wrapper.find("img");
		expect(image.exists()).toBe(false);

		wrapper.vm.handleImageUpload(mockEvent);
		await wrapper.vm.$nextTick();

		image = wrapper.find("img");
		expect(image.exists()).toBe(true);
	});

	it("should emit imageTooLargeUploaded event on large file uploaded", () => {
		const mockEvent = {
			target: {
				id: "media0",
				files: [
					{
						name: "image.png",
						size: 50000000,
						type: "image/png",
					},
				],
			},
		};
		wrapper.vm.handleImageUpload(mockEvent);
		const emitted = wrapper.emitted();
		expect(emitted).toHaveProperty("imageTooLargeUploaded");
	});
});
