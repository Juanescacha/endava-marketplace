import { beforeEach, describe, expect, it, test, vi } from "vitest";
import { mount, shallowMount, flushPromises } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import * as axios from "@/utils/axios";
import { useQuestionSection } from "@/stores/questionSection";
import IndividualQuestion from "@/components/QuestionsSection/IndividualQuestion.vue";

const longQuestion = {
	id: -1,
	question_detail:
		"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque in urna a elit",
	question_date: "2024-01-01",
	buyer: { name: "John Doe" },
	answer_detail: null,
	answer_date: null,
};

const shortQuestion = {
	id: -1,
	question_detail: "Is this big?",
	question_date: "2024-01-01",
	buyer: { name: "John Doe" },
	answer_detail: null,
	answer_date: null,
};

const longAnswer = {
	id: -1,
	question_detail: "Is this big?",
	question_date: "2024-01-01",
	buyer: { name: "John Doe" },
	answer_detail:
		"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque in urna a elit",
	answer_date: "2024-01-01",
};

const shortAnswer = {
	id: -1,
	question_detail: "Is this big?",
	question_date: "2024-01-01",
	buyer: { name: "John Doe" },
	answer_detail: "Lorem ipsum dolor sit amet",
	answer_date: "2024-01-01",
};

const QUESTION_P_SELECTOR = "[data-ui='question-paragraph']";
const VIEW_MORE_QUESTION_SELECTOR = "[data-ui='view-more-question']";
const ANSWER_P_SELECTOR = "[data-ui='answer-paragraph']";
const VIEW_MORE_ANSWER_SELECTOR = "[data-ui='view-more-answer']";
const ANSWER_BTN_SELECTOR = "[data-ui='answer-button']";
const ANSWER_FROM_SELECTOR = "[data-ui='answer-form']";

describe("IndividualQuestion render", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(IndividualQuestion, {
			props: { question: shortAnswer },
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});
	it("should mount", () => {
		expect(IndividualQuestion).toBeTruthy();
		expect(wrapper.exists()).toBe(true);
	});

	it("should render the information from the props", () => {
		expect(wrapper.html()).toContain(shortAnswer.question_detail);
		expect(wrapper.html()).toContain(shortAnswer.question_date);
		expect(wrapper.html()).toContain(shortAnswer.answer_detail);
		expect(wrapper.html()).toContain(shortAnswer.answer_date);
		expect(wrapper.html()).toContain(shortAnswer.buyer.name);
	});
});

describe("IndividualQuestion question behaviour", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(IndividualQuestion, {
			props: { question: longQuestion },
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});

	/**
	 * Note:
	 * A "long" question/answer has more than 75 characters. A "short" question/answer has 75 or less.
	 */

	it("should not render the full question if it's too long", () => {
		const questionParagraph = wrapper.find(QUESTION_P_SELECTOR);
		expect(questionParagraph.classes()).toContain("truncate");
	});

	it("should render a 'View more' button when the question is too long", () => {
		expect(wrapper.html()).toContain("View more");
	});

	it("should not render a 'View more' or 'View less' button when the question is short", () => {
		const wrapper = mount(IndividualQuestion, {
			props: { question: shortQuestion },
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
		expect(wrapper.html()).not.toContain("View more");
		expect(wrapper.html()).not.toContain("View less");
	});

	it("should render a full long question after pressing 'View more'", async () => {
		await wrapper.find(VIEW_MORE_QUESTION_SELECTOR).trigger("click");

		const questionParagraph = wrapper.find(QUESTION_P_SELECTOR);
		expect(questionParagraph.classes()).not.toContain("truncate");
	});

	it("should toogle the text of the 'View more' button after being pressed", async () => {
		expect(wrapper.html()).toContain("View more");
		expect(wrapper.html()).not.toContain("View less");

		await wrapper.find(VIEW_MORE_QUESTION_SELECTOR).trigger("click");

		expect(wrapper.html()).not.toContain("View more");
		expect(wrapper.html()).toContain("View less");

		await wrapper.find(VIEW_MORE_QUESTION_SELECTOR).trigger("click");

		expect(wrapper.html()).toContain("View more");
		expect(wrapper.html()).not.toContain("View less");
	});
});

describe("IndividualQuestion answer section behaviour when there is an answer", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(IndividualQuestion, {
			props: { question: longAnswer },
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});

	it("should not render the full question if it's too long", () => {
		const answerParagraph = wrapper.find(ANSWER_P_SELECTOR);
		expect(answerParagraph.classes()).toContain("truncate");
	});

	it("should render a 'View more' button when the question is too long", () => {
		expect(wrapper.html()).toContain("View more");
	});

	it("should not render a 'View more' or 'View less' button when the question is short", () => {
		const wrapper = mount(IndividualQuestion, {
			props: { question: shortAnswer },
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
		expect(wrapper.html()).not.toContain("View more");
		expect(wrapper.html()).not.toContain("View less");
	});

	it("should render a full long question after pressing 'View more'", async () => {
		await wrapper.find(VIEW_MORE_ANSWER_SELECTOR).trigger("click");

		const answerParagraph = wrapper.find(ANSWER_P_SELECTOR);
		expect(answerParagraph.classes()).not.toContain("truncate");
	});

	it("should toogle the text of the 'View more' button after being pressed", async () => {
		expect(wrapper.html()).toContain("View more");
		expect(wrapper.html()).not.toContain("View less");

		await wrapper.find(VIEW_MORE_ANSWER_SELECTOR).trigger("click");

		expect(wrapper.html()).not.toContain("View more");
		expect(wrapper.html()).toContain("View less");

		await wrapper.find(VIEW_MORE_ANSWER_SELECTOR).trigger("click");

		expect(wrapper.html()).toContain("View more");
		expect(wrapper.html()).not.toContain("View less");
	});
});

describe("IndividualQuestion answer section behaviour when there is no answer", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(IndividualQuestion, {
			props: { question: shortQuestion },
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});

		const questionSection = useQuestionSection();
		questionSection.isUserTheSeller = true;
	});

	test("there should be an 'Answer' button to show the answer form whn the user is the seller", () => {
		const btn = wrapper.find(ANSWER_BTN_SELECTOR);
		expect(btn.exists()).toBe(true);
	});

	test("When 'Answer' button is clicked a form is rendered", async () => {
		let form = wrapper.find(ANSWER_FROM_SELECTOR);
		expect(form.exists(), "the form is not rendered yet").toBe(false);

		wrapper.find(ANSWER_BTN_SELECTOR).trigger("click");
		await wrapper.vm.$nextTick();

		form = wrapper.find(ANSWER_FROM_SELECTOR);
		expect(form.exists(), "the form is rendered").toBe(true);
	});

	test("when the user is not the seller, the 'Answer' button should not be rendered", () => {
		const wrapper = mount(IndividualQuestion, {
			props: { question: shortQuestion },
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});

		const btn = wrapper.find(ANSWER_BTN_SELECTOR);
		expect(btn.exists()).toBe(false);

		wrapper.unmount();
	});
});

describe("IndividualQuestion answer form behaviour", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = mount(IndividualQuestion, {
			props: { question: shortQuestion },
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
		const questionSection = useQuestionSection();
		questionSection.isUserTheSeller = true;
	});

	const successfulResponse = { answer_detail: "foo", answer_date: "bar" };

	const showAnswerForm = async () => {
		if (wrapper) {
			wrapper.find(ANSWER_BTN_SELECTOR).trigger("click");
			await wrapper.vm.$nextTick();
		}
	};

	const makeFormValid = () => {
		if (wrapper) {
			wrapper.vm.answer = "Valid answer";
		}
	};

	it("should call postAnswer", async () => {
		await showAnswerForm();
		makeFormValid();
		const postSpy = vi.spyOn(axios, "postAnswer");

		wrapper.vm.handleAnswerSubmit();
		expect(postSpy).toHaveBeenCalled();
	});

	it("should emit questionAnswered when the operation is succesful", async () => {
		await showAnswerForm();
		makeFormValid();
		vi.spyOn(axios, "postAnswer").mockImplementationOnce(() => ({
			data: successfulResponse,
		}));

		wrapper.vm.handleAnswerSubmit();
		await flushPromises();
		expect(wrapper.emitted()).toHaveProperty("questionAnswered");
	});

	it("should emit questionAnswered with the question id and aswer details", async () => {
		await showAnswerForm();
		makeFormValid();
		vi.spyOn(axios, "postAnswer").mockImplementationOnce(() => ({
			data: successfulResponse,
		}));

		wrapper.vm.handleAnswerSubmit();
		await flushPromises();

		const payload = wrapper.emitted().questionAnswered[0][0];
		expect(payload.id, "passes the question id").toBe(shortQuestion.id);
		expect(payload.answer.detail, "passes the answer").toBe(
			successfulResponse.answer_detail
		);
		expect(payload.answer.date, "passes the answer date").toBe(
			successfulResponse.answer_date
		);
	});
});
