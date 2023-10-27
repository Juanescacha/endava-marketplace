import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";
import TeamsChatLink from "@/components/TeamsChatLink.vue";

const props = {
	user: "John@email.com",
	innerText: "Click",
};

describe("TeamsChatLink mounts", () => {
	const wrapper = mount(TeamsChatLink, { props });

	it("should mount", () => {
		expect(TeamsChatLink).toBeTruthy();
	});

	it("should mount with the provided 'user' prop", () => {
		expect(wrapper.props("user")).toBe(props.user);
	});

	it("should mount with the provided 'innerText' prop", () => {
		expect(wrapper.props("innerText")).toBe(props.innerText);
	});
	wrapper.unmount();
});

it("should mount with the default 'innerText' prop", () => {
	const wrapper = mount(TeamsChatLink, { props: { user: "foo" } });
	expect(wrapper.props("innerText")).toBe("Let's chat!");
	wrapper.unmount();
});

describe("TeamsChatLink 'a' tag", () => {
	const wrapper = mount(TeamsChatLink, { props });
	const aTag = wrapper.find("a");

	it("should exist", () => {
		expect(aTag.exists()).toBe(true);
	});

	it("should contain the innerText", () => {
		expect(aTag.html()).toContain(wrapper.props("innerText"));
	});

	it("should have a href attribute that point to microsoft teams chat", () => {
		expect(aTag.attributes()).toHaveProperty("href");
		expect(aTag.attributes().href).toContain(
			"https://teams.microsoft.com/l/chat/0/0?users"
		);
		expect(aTag.attributes().href).toContain(wrapper.props("user"));
	});

	it("should have a target attribute", () => {
		expect(aTag.attributes()).toHaveProperty("target");
		expect(aTag.attributes().target).toBe("_blank");
	});

	it("should have a rel attribute", () => {
		expect(aTag.attributes()).toHaveProperty("rel");
		expect(aTag.attributes().rel).toBe("noopener noreferrer");
	});

	wrapper.unmount();
});
