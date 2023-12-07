<script setup>
	import { useQuestionSection } from "@/stores/questionSection";
	import NewQuestionForm from "./NewQuestionForm.vue";
	import LatestQuestions from "./LatestQuestions.vue";

	const questionSection = useQuestionSection();
</script>

<template>
	<div class="w-full max-w-xl px-8 md:px-0">
		<h2 class="mb-2">Item Q&A</h2>
		<new-question-form
			v-if="!questionSection.isUserTheSeller"
		></new-question-form>
		<latest-questions></latest-questions>
	</div>
</template>
