import { afterEach, beforeEach, describe, expect, it, test } from "vitest";
import { mount } from "@vue/test-utils";
import StarSVG from "@/components/Icons/StarSVG.vue";
import fillings from "@/assets/starSVG";

const minimalProps = {
	starId: 1,
};

const props = {
	size: 2,
	color: "#f3f3f3",
	starId: 0,
	fillAmount: "half",
};

describe("StarSVG mounts with default value props", () => {
	const wrapper = mount(StarSVG, { props: minimalProps });

	it("should mount", () => {
		expect(StarSVG).toBeTruthy();
	});

	it("should mount with the default 'size' prop", () => {
		expect(wrapper.props("size")).toBe(1);
	});

	it("should mount with the default 'color' prop", () => {
		expect(wrapper.props("color")).toBe("#000000");
	});

	it("should mount with the default 'fillAmount' prop", () => {
		expect(wrapper.props("fillAmount")).toBe("empty");
	});
	wrapper.unmount();
});

describe("StarSVG mount with provided values", () => {
	const wrapper = mount(StarSVG, { props });

	it("should mount with the size prop", () => {
		expect(wrapper.props("size")).toBe(props.size);
	});

	it("should mount with the color prop", () => {
		expect(wrapper.props("color")).toBe(props.color);
	});

	it("should mount with the starId prop", () => {
		expect(wrapper.props("starId")).toBe(props.starId);
	});

	it("should mount with the fillAmount prop", () => {
		expect(wrapper.props("fillAmount")).toBe(props.fillAmount);
	});
	wrapper.unmount();
});

describe("StarSVG prop validation", () => {
	const sizeValidator = StarSVG.props.size.validator;
	const fillValidator = StarSVG.props.fillAmount.validator;

	test("size validator should return true on positive numbers", () => {
		expect(sizeValidator(1)).toBe(true);
		expect(sizeValidator(0.01)).toBe(true);
		expect(sizeValidator(100000)).toBe(true);
	});

	test("size validator should return false on 0 and negative numbers", () => {
		expect(sizeValidator(-0.001)).toBe(false);
		expect(sizeValidator(-1)).toBe(false);
		expect(sizeValidator(-100000)).toBe(false);
	});

	test("size validator should handle strings", () => {
		expect(sizeValidator("foo")).toBe(false);
		expect(sizeValidator("1foo")).toBe(false);
		expect(sizeValidator("1")).toBe(true);
		expect(sizeValidator("-1")).toBe(false);
	});

	test("fillAmount validator should return true on a valid fill amount", () => {
		const validFillings = Object.keys(fillings);
		validFillings.forEach(filling => {
			expect(fillValidator(filling)).toBe(true);
		});
	});

	test("fillAmount validator should return false on any other input", () => {
		expect(fillValidator("Foo")).toBe(false);
		expect(fillValidator(1)).toBe(false);
		expect(fillValidator({})).toBe(false);
	});
});

describe("StarSVG HTML tags", () => {
	const wrapper = mount(StarSVG, { props });

	const svg = wrapper.find("svg");
	const path = wrapper.find("path");

	it("should have a svg tag", () => {
		expect(svg.exists()).toBe(true);
	});

	it("the svg should be of the appropiate fill color", () => {
		expect(svg.attributes().fill).toBe(wrapper.props("color"));
	});

	it("the svg should have a width and height", () => {
		expect(svg.attributes()).toHaveProperty("width");
		expect(svg.attributes()).toHaveProperty("height");
	});

	it("should have a path tag", () => {
		expect(path.exists()).toBe(true);
	});

	it("the path's d attributte should equal the filling provided in starSVG", () => {
		expect(path.attributes().d).toBe(fillings[props.fillAmount]);
	});
	wrapper.unmount();
});

describe("StarSVG events", () => {
	let wrapper = null;
	beforeEach(() => (wrapper = mount(StarSVG, { props })));
	afterEach(() => wrapper.unmount());

	it("should emit starHover on mousemove", async () => {
		const svg = wrapper.find("svg");
		await svg.trigger("mousemove");

		const emitted = wrapper.emitted();
		expect(emitted).toHaveProperty("starHover");
	});

	it("should emit starHover with payload", async () => {
		const svg = wrapper.find("svg");
		await svg.trigger("mousemove");

		const payload = wrapper.emitted().starHover[0][0];
		expect(payload).toHaveProperty("starId");
		expect(payload).toHaveProperty("mousePosition");
	});

	it("should emit starClicked on click", async () => {
		const svg = wrapper.find("svg");
		await svg.trigger("click");

		const emitted = wrapper.emitted();
		expect(emitted).toHaveProperty("starClicked");
	});

	it("should emit starClicked with payload", async () => {
		const svg = wrapper.find("svg");
		await svg.trigger("click");

		const payload = wrapper.emitted().starClicked[0][0];
		expect(payload).toHaveProperty("starId");
		expect(payload).toHaveProperty("mousePosition");
	});
});
