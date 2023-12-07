import { beforeEach, describe, expect, it, vi } from "vitest";
import { shallowMount } from "@vue/test-utils";
import { createTestingPinia } from "@pinia/testing";
import * as axios from "@/utils/axios";
import LatestQuestions from "@/components/QuestionsSection/LatestQuestions.vue";

const mockQuestions = [
	{ id: 1, question_detail: "foo" },
	{ id: 2, question_detail: "bar" },
	{ id: 3, question_detail: "lorem" },
];

it("LatestQuestions should mount", () => {
	const wrapper = shallowMount(LatestQuestions, {
		global: {
			plugins: [createTestingPinia({ createSpy: vi.fn() })],
		},
	});
	expect(LatestQuestions, "the component exist").toBeTruthy();
	expect(wrapper.exists(), "the component mounts").toBe(true);
});

describe("LatestQuestions question fetching", () => {
	let wrapper;
	beforeEach(() => {
		wrapper = shallowMount(LatestQuestions, {
			global: {
				plugins: [createTestingPinia({ createSpy: vi.fn() })],
			},
		});
	});

	it("should call getListingQuestions", () => {
		const fetchSpy = vi
			.spyOn(axios, "getListingQuestions")
			.mockImplementationOnce(() => ({ data: mockQuestions }));
		wrapper.vm.fetchQuestions();
		expect(fetchSpy).toHaveBeenCalled();
	});

	it("should populate the questions list", async () => {
		const { fetchQuestions, questions } = wrapper.vm;
		vi.spyOn(axios, "getListingQuestions").mockImplementationOnce(() => ({
			data: mockQuestions,
		}));

		expect(questions.length, "the questions list should start empty").toBe(
			0
		);
		await fetchQuestions();
		expect(
			questions.length,
			"the questions list should contain as many elements as mockQuestions"
		).toBe(mockQuestions.length);
	});

	it("should populate the questions list with the returned elements", async () => {
		const { fetchQuestions, questions } = wrapper.vm;
		vi.spyOn(axios, "getListingQuestions").mockImplementationOnce(() => ({
			data: mockQuestions,
		}));

		await fetchQuestions();
		expect(
			questions[0].id,
			"the 1st element should have the same id as the response"
		).toBe(mockQuestions[0].id);
		expect(
			questions[1].id,
			"the 2st element should have the same id as the response"
		).toBe(mockQuestions[1].id);
	});

	it("should populate the html with li elements", async () => {
		const { fetchQuestions } = wrapper.vm;
		vi.spyOn(axios, "getListingQuestions").mockImplementationOnce(() => ({
			data: mockQuestions,
		}));

		await fetchQuestions();
		expect(wrapper.findAll("li").length).toBe(mockQuestions.length);
	});
});
