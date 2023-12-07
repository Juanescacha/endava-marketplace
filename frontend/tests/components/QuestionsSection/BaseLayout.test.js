import { describe, expect, test, vi } from "vitest";
import { mount, shallowMount } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import NewQuestionForm from "@/components/QuestionsSection/NewQuestionForm.vue";
import LatestQuestions from "@/components/QuestionsSection/LatestQuestions.vue";
import BaseLayout from "@/components/QuestionsSection/BaseLayout.vue";

test("BaseLayout should mount", () => {
	const wrapper = shallowMount(BaseLayout, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
		},
	});
	expect(BaseLayout).toBeTruthy();
	expect(wrapper.exists()).toBe(true);
	wrapper.unmount();
});

describe("QuestionsSection BaseLayout components", () => {
	const wrapper = mount(BaseLayout, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
		},
	});

	test("BaseLayout has a NewQuestionForm component", () => {
		const form = wrapper.findComponent(NewQuestionForm);
		expect(form.exists()).toBe(true);
	});

	test("BaseLayout has a LatestQuestions component", () => {
		const questions = wrapper.findComponent(LatestQuestions);
		expect(questions.exists()).toBe(true);
	});
});
