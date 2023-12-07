<script setup>
	import { computed, onBeforeMount, reactive, watch } from "vue";
	import { useQuestionSection } from "@/stores/questionSection";
	import IndividualQuestion from "./IndividualQuestion.vue";
	import { getListingQuestions } from "@/utils/axios";

	const questions = reactive([]);
	const questionSection = useQuestionSection();
	const questionsAreUpdatable = computed(
		() => questionSection.updateLatestQuestion
	);

	onBeforeMount(() => {
		fetchQuestions();
	});

	watch(questionsAreUpdatable, () => {
		if (questionsAreUpdatable.value) {
			fetchQuestions();
		}
	});

	const fetchQuestions = async () => {
		const { data } = await getListingQuestions(questionSection.listingId);
		if (data) {
			questions.length = 0;
			data.forEach(question => {
				questions.push(question);
			});
		}
	};

	const updateAnswer = ({ id, answer }) => {
		const question = questions.find(question => question.id === id);
		question.answer_detail = answer.detail;
		question.answer_date = answer.date;
	};
</script>

<template>
	<h3>Latest questions</h3>
	<ul v-if="questions.length > 0">
		<li
			v-for="question in questions"
			:key="question.id"
			class="mb-3 border-b-2 p-4"
		>
			<IndividualQuestion
				:question="question"
				@question-answered="updateAnswer"
			/>
		</li>
	</ul>
	<p v-else>No questions yet</p>
</template>
