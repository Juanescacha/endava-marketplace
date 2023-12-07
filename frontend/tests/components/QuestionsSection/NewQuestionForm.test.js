import { afterEach, beforeEach, describe, expect, it, test, vi } from "vitest";
import { mount, shallowMount, flushPromises } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import { useQuestionSection } from "@/stores/questionSection";
import * as axios from "@/utils/axios";
import NewQuestionForm from "@/components/QuestionsSection/NewQuestionForm.vue";

const VALID_QUESTION = "valid question";
const INVALID_QUESTION = "val";

test("NewQuestionForm should mount", () => {
	const wrapper = shallowMount(NewQuestionForm, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
		},
	});
	expect(NewQuestionForm).toBeTruthy();
	expect(wrapper.exists()).toBe(true);
	wrapper.unmount();
});

describe("NewQuestionForm handleInputChanged", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(NewQuestionForm, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});

	afterEach(() => wrapper.unmount());

	it("should update the form data", () => {
		const { INPUT_ID, handleInputChanged, formData } = wrapper.vm;

		handleInputChanged({
			target: { value: VALID_QUESTION, id: INPUT_ID },
		});

		expect(formData[INPUT_ID].value).toBe(VALID_QUESTION);
	});

	it("should display an error when passed the textarea has an invalid length for the first time", async () => {
		const { INPUT_ID, INVALID_INPUT_ERROR, handleInputChanged, $nextTick } =
			wrapper.vm;

		handleInputChanged({
			target: { value: INVALID_QUESTION, id: INPUT_ID },
		});

		await $nextTick();
		expect(wrapper.html()).not.toContain(INVALID_INPUT_ERROR);
	});

	it("should display an error when passed the textarea has an invalid length after a valid length", async () => {
		const { INPUT_ID, INVALID_INPUT_ERROR, handleInputChanged, $nextTick } =
			wrapper.vm;

		handleInputChanged({
			target: { value: VALID_QUESTION, id: INPUT_ID },
		});

		handleInputChanged({
			target: { value: INVALID_QUESTION, id: INPUT_ID },
		});

		await $nextTick();
		expect(wrapper.html()).toContain(INVALID_INPUT_ERROR);
	});
});

describe("NewQuestionForm form submit", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(NewQuestionForm, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});

		const questionSection = useQuestionSection();
		questionSection.triggerUpdateOnLatestQuestion = vi.fn();
	});

	afterEach(() => wrapper.unmount());

	const makeQuestionValid = () => {
		if (wrapper) {
			wrapper.vm.handleInputChanged({
				target: { value: VALID_QUESTION, id: wrapper.vm.INPUT_ID },
			});
		}
	};
	it("should show an error when the question is not valid", async () => {
		const {
			INPUT_ID,
			INVALID_INPUT_ERROR,
			handleInputChanged,
			handleFormSubmit,
			$nextTick,
		} = wrapper.vm;

		handleInputChanged({
			target: { value: INVALID_QUESTION, id: INPUT_ID },
		});
		handleFormSubmit();
		await $nextTick();

		expect(wrapper.html()).toContain(INVALID_INPUT_ERROR);
	});

	it("should call postQuestion function", () => {
		const { handleFormSubmit } = wrapper.vm;
		const postSpy = vi
			.spyOn(axios, "postQuestion")
			.mockImplementationOnce(() => ({ error: true }));

		makeQuestionValid();
		handleFormSubmit();

		expect(postSpy).toHaveBeenCalled();
	});

	it("should show an error when the postQuestion returns an error", async () => {
		const response = { error: true, msg: "error msg" };
		const { handleFormSubmit } = wrapper.vm;
		vi.spyOn(axios, "postQuestion").mockImplementationOnce(() => response);

		makeQuestionValid();
		handleFormSubmit();

		await flushPromises();
		expect(wrapper.html()).toContain(response.msg);
	});

	it("should show an error when the postQuestion returns an error", async () => {
		const response = { data: {}, msg: "success msg" };
		const { handleFormSubmit } = wrapper.vm;
		vi.spyOn(axios, "postQuestion").mockImplementationOnce(() => response);

		makeQuestionValid();
		handleFormSubmit();

		await flushPromises();
		expect(wrapper.html()).toContain(response.msg);
	});
});
