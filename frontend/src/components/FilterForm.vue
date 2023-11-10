<script setup>
	import { reactive } from "vue";
	import TextInput from "@/components/Inputs/TextInput.vue";
	import FormButton from "@/components/Inputs/FormButton.vue";

	const formData = reactive({ name: "", email: "" });
	const emit = defineEmits(["filteredSearch"]);

	const handleInputChange = $event => {
		formData[$event.id] = $event.value;
	};

	const handleFormSubmit = () => {
		emit("filteredSearch", formData);
	};
</script>

<template>
	<form @submit.prevent="handleFormSubmit">
		<div class="flex gap-4">
			<text-input
				name="name"
				placeholder="Name"
				containerClass="w-48 my-4"
				@input-changed="handleInputChange"
			/>
			<text-input
				name="email"
				placeholder="Email"
				containerClass="w-48 my-4"
				@input-changed="handleInputChange"
			/>
		</div>
		<form-button
			class="rounded-full"
			btn-type="submit"
			text="Filter"
		></form-button>
	</form>
</template>
