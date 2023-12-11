<script setup>
	import { ref } from "vue";
	import { useQuestionSection } from "@/stores/questionSection";
	import LShape from "@/components/Icons/LShape.vue";
	import TextAreaWithCounter from "@/components/Inputs/TextAreaWithCounter.vue";
	import { postAnswer } from "../../utils/axios";

	const props = defineProps({
		question: {
			type: Object,
			required: true,
		},
	});

	const emit = defineEmits(["questionAnswered"]);

	const TRUNCATE_LENGTH = 75;
	const answerFormIsVisible = ref(false);
	const answer = ref("");
	const error = ref(null);
	const questionSection = useQuestionSection();

	const isFullViewQuestion = ref(
		props.question.question_detail.length <= TRUNCATE_LENGTH
	);
	const isFullViewAnswer = ref(
		props.question.answer?.length <= TRUNCATE_LENGTH - 2
	);

	const toogleAnswerForm = () => {
		answerFormIsVisible.value = !answerFormIsVisible.value;
	};

	const handleInputChanged = ({ target: { value } }) => {
		answer.value = value;
	};

	const validateAnswer = () => {
		const isValid = answer.value.length > 1 && answer.value.length <= 500;
		if (!isValid) {
			error.value = `The answer must have between 2 and 500 characters`;
			setTimeout(() => {
				error.value = null;
			}, 4000);
		}
		return isValid;
	};

	const handleAnswerSubmit = async () => {
		const isValidAnswer = validateAnswer();

		if (!isValidAnswer) {
			return;
		}

		const { data, error: requestError } = await postAnswer({
			id: props.question.id,
			answer_detail: answer.value,
		});

		if (!requestError) {
			emit("questionAnswered", {
				id: props.question.id,
				answer: {
					detail: data.answer_detail,
					date: data.answer_date,
				},
			});
		} else {
			error.value = "Failed connection to the server";
			setTimeout(() => {
				error.value = null;
			}, 4000);
		}
	};
</script>

<template>
	<div>
		<div class="flex items-center">
			<h4>{{ question.buyer.name }}</h4>
			<p class="text-sm text-gray-500">
				<time :datetime="question.question_date"
					>&emsp;({{ question.question_date }})</time
				>
			</p>
		</div>

		<p
			class="pl-2"
			id="question-paragraph"
			data-ui="question-paragraph"
			:class="isFullViewQuestion ? '' : 'truncate'"
		>
			{{ question.question_detail }}
		</p>
		<button
			v-if="question.question_detail.length > TRUNCATE_LENGTH"
			type="button"
			class="text-sm text-gray-500 underline"
			data-ui="view-more-question"
			@click="isFullViewQuestion = !isFullViewQuestion"
		>
			{{ !isFullViewQuestion ? "View more" : "View less" }}
		</button>

		<div
			v-if="question.answer_detail"
			class="ml-2 mt-4"
		>
			<div class="flex items-start">
				<LShape class="mr-2" />
				<p
					class="px-4 py-1"
					:class="isFullViewAnswer ? '' : 'truncate'"
					id="answer-paragraph"
					data-ui="answer-paragraph"
				>
					{{ question.answer_detail }}
				</p>
			</div>
			<p class="ml-9 text-sm text-gray-600">
				<time :datetime="question.answer_date">
					{{ question.answer_date }}
				</time>
			</p>
			<button
				v-if="question.answer_detail.length > TRUNCATE_LENGTH"
				type="button"
				class="ml-9 block text-sm text-gray-500 underline"
				data-ui="view-more-answer"
				@click="isFullViewAnswer = !isFullViewAnswer"
			>
				{{ !isFullViewAnswer ? "View more" : "View less" }}
			</button>
		</div>

		<div
			v-else-if="
				questionSection.isUserTheSeller && !question.answer_detail
			"
			class="mt-2 flex flex-col items-center"
		>
			<button
				v-if="!answerFormIsVisible"
				data-ui="answer-button"
				class="endava w-24 py-1"
				@click="toogleAnswerForm"
			>
				Answer
			</button>
			<form
				v-else
				class="flex w-4/5 flex-col items-center"
				:id="`${question.buyer}-answer-form`"
				data-ui="answer-form"
				@submit.prevent="handleAnswerSubmit"
			>
				<text-area-with-counter
					name="answer-textarea"
					class="w-full"
					@input-changed="handleInputChanged"
				/>
				<p
					v-if="error"
					class="text-sm text-red-600"
					data-ui="error-p"
				>
					{{ error }}
				</p>
				<div class="mt-2">
					<button
						type="submit"
						class="endava mx-2 px-3 py-1"
					>
						Answer
					</button>
					<button
						type="button"
						class="mx-2 rounded-md bg-gray-600 px-3 py-1 text-white"
						@click="toogleAnswerForm"
					>
						Cancel
					</button>
				</div>
			</form>
		</div>
	</div>
</template>
