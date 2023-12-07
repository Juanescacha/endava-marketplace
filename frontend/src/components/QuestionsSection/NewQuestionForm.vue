<script setup>
	import { ref } from "vue";
	import { useUserStore } from "@/stores/user";
	import { useQuestionSection } from "@/stores/questionSection";
	import { postQuestion } from "@/utils/axios";
	import useForm from "@/composables/useForm";
	import useNotification from "@/composables/useNotification";
	import TextAreaWithCounter from "@/components/Inputs/TextAreaWithCounter.vue";
	import NotificationBox from "@/components/NotificationBox.vue";

	const INPUT_ID = "myQuestion";
	const canBeWarned = ref(false);
	const user = useUserStore();
	const questionSection = useQuestionSection();
	const { showMsg, msgColor, displayMsg } = useNotification();
	const { formData, decorateFormData, handleTextInput, validateTextInput } =
		useForm();

	decorateFormData([INPUT_ID]);
	formData[INPUT_ID].min = 5;
	formData[INPUT_ID].max = 500;

	const INVALID_INPUT_ERROR = `The question must have between ${formData[INPUT_ID].min} and ${formData[INPUT_ID].max} characters`;

	const handleInputChanged = $event => {
		handleTextInput($event);
		const isValid = validateTextInput(
			$event.target.id,
			$event.target.value
		);
		if (!canBeWarned.value && $event.target.value.length > 5) {
			canBeWarned.value = true;
		}
		if (!isValid && canBeWarned.value) {
			displayMsg(INVALID_INPUT_ERROR, "red");
		}
	};

	const handleFormSubmit = async () => {
		if (!formData[INPUT_ID].valid) {
			displayMsg(INVALID_INPUT_ERROR, "red");
			return;
		}
		const data = {
			listing_id: questionSection.listingId,
			buyer_id: user.id,
			question_detail: formData[INPUT_ID].value,
		};

		const result = await postQuestion(data);

		if (result.error) {
			displayMsg(result.msg, "red");
			return;
		}

		displayMsg(result.msg, "green");
		resetForm();
		questionSection.triggerUpdateOnLatestQuestion();
	};

	const resetForm = () => {
		canBeWarned.value = false;
		const textarea = document.getElementById(INPUT_ID);
		if (textarea) {
			textarea.value = "";
			textarea.dispatchEvent(new Event("input", { bubbles: false }));
		}
	};
</script>

<template>
	<form
		class="my-4 flex flex-col gap-2"
		@submit.prevent="handleFormSubmit"
		id="new-question-form"
	>
		<h3>Ask the seller!</h3>
		<text-area-with-counter
			:name="INPUT_ID"
			placeholder="Type your question!"
			@input-changed="handleInputChanged"
		/>
		<notification-box
			:message="showMsg"
			:color="msgColor"
		/>

		<button
			type="submit"
			class="endava mx-auto h-8 w-3/5"
		>
			Ask a Question
		</button>
	</form>
</template>
