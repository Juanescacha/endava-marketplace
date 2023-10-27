import {
	afterEach,
	beforeEach,
	describe,
	expect,
	expectTypeOf,
	it,
} from "vitest";
import { mount } from "@vue/test-utils";
import ImageSelectorList from "@/components/Images/ImageSelectorList.vue";

const mockImages = [
	"https://foo.webm/",
	"https://bar.webm/",
	"https://john.webm/",
];

const props = { images: mockImages };

describe("ImageSelectorList mounts with props", () => {
	const wrapper = mount(ImageSelectorList, {
		props: {
			images: mockImages,
			styles: "foo",
		},
	});

	it("should mount", () => {
		expect(ImageSelectorList).toBeTruthy();
	});

	it("should mount with the 'images' prop", () => {
		expectTypeOf(wrapper.props("images")).toBeArray();
	});

	it("should mount with the 'styles' prop", () => {
		expect(wrapper.props("styles")).toBe("foo");
	});

	wrapper.unmount();
});

describe("ImageSelectorList mounts with default value props", () => {
	const wrapper = mount(ImageSelectorList, { props });

	it("should mount with the 'styles' prop", () => {
		expect(wrapper.props("styles")).toBeUndefined();
	});

	wrapper.unmount();
});

describe("ImageSelectorList HTML tags", () => {
	let wrapper = null;
	beforeEach(() => {
		wrapper = mount(ImageSelectorList, { props });
	});
	afterEach(() => wrapper.unmount());

	it("should have an img element", () => {
		expect(wrapper.find("img").exists()).toBe(true);
	});
	it("the img should have a src attribute", () => {
		const img = wrapper.find("img");
		expect(img.attributes().src).toBe(mockImages[0]);
	});
	it("the img should have an alt attribute", () => {
		const img = wrapper.find("img");
		expect(img.attributes().alt).toBeTruthy();
	});

	it("should have a ul element", () => {
		expect(wrapper.find("ul").exists()).toBe(true);
	});

	it("should have three li element", () => {
		const li = wrapper.findAll("li");
		expect(li.length).toBe(3);
	});

	it("li elements should have orange border when selected", async () => {
		const wrapper = mount(ImageSelectorList, { props });
		const imgs = wrapper.findAll("img");
		expect(imgs[1].classes()).toContain("border-gray-400");

		await imgs[1].trigger("click");

		expect(imgs[1].classes()).toContain("border-endava-600");
	});
});

describe("ImageSelectorList emitted events", () => {
	let wrapper = null;
	beforeEach(() => {
		wrapper = mount(ImageSelectorList, { props });
	});
	afterEach(() => wrapper.unmount());

	it("should emit image-selected event", async () => {
		const imgs = wrapper.findAll("img");
		await imgs[0].trigger("click");
		const emittedEvents = wrapper.emitted();
		expect(emittedEvents["image-selected"]).toBeDefined();
	});

	it("should emit current index", async () => {
		const imgs = wrapper.findAll("img");
		await imgs[1].trigger("click");
		const payload = wrapper.emitted()["image-selected"][0][0];
		expect(payload).toBe(imgs[1].element.src);
	});
});
