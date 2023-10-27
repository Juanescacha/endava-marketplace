import {
	afterEach,
	beforeEach,
	describe,
	expect,
	expectTypeOf,
	it,
} from "vitest";
import { mount } from "@vue/test-utils";
import ImageSelector from "@/components/Images/ImageSelector.vue";
import ImageSelectorList from "@/components/Images/ImageSelectorList.vue";

const mockImages = [
	"https://foo.webm/",
	"https://bar.webm/",
	"https://john.webm/",
];

const props = { images: mockImages };

describe("ImageSelector mounts with props", () => {
	const wrapper = mount(ImageSelector, {
		props: {
			images: mockImages,
			styles: "foo",
		},
	});

	it("should mount", () => {
		expect(ImageSelector).toBeTruthy();
	});

	it("should mount with the 'images' prop", () => {
		expectTypeOf(wrapper.props("images")).toBeArray();
	});

	it("should mount with the 'styles' prop", () => {
		expect(wrapper.props("styles")).toBe("foo");
	});

	wrapper.unmount();
});

describe("ImageSelector HTML tags", () => {
	let wrapper = null;
	beforeEach(() => {
		wrapper = mount(ImageSelector, { props });
	});
	afterEach(() => wrapper.unmount());

	it("should have an img element", () => {
		const img = wrapper.find("img");
		expect(img.exists()).toBe(true);
	});

	it("the img should have a src attribute", () => {
		const img = wrapper.find("img");
		expect(img.element.src).toBe(mockImages[0]);
	});

	it("the img should have an alt attribute", () => {
		const img = wrapper.find("img");
		expect(img.element.alt).toBeTruthy();
	});

	it("should have an ImageSelectorList element", () => {
		const imageSelector = wrapper.findComponent(ImageSelectorList);
		expect(imageSelector.exists()).toBe(true);
	});
});

describe("ImageSelector handles emitted events", () => {
	let wrapper = null;
	beforeEach(() => {
		wrapper = mount(ImageSelector, { props });
	});
	afterEach(() => wrapper.unmount());

	it("should update the image on image-selected", async () => {
		const img = wrapper.find("img");
		const imageSelectorList = wrapper.findComponent(ImageSelectorList);

		expect(img.element.src).toBe(mockImages[0]);

		await imageSelectorList.vm.$emit("image-selected", mockImages[1]);

		expect(img.element.src).toBe(mockImages[1]);
	});
});
