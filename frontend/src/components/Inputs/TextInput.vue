<script setup>
import { ref, computed } from "vue";

const error = ref(null);
const cssClasses = computed(() => {
	if (!error.value) {
		return "rounded border border-zinc-400 px-2 py-3";
	}
	return "rounded border border-red-500 px-2 py-3 text-red-500";
});

const emit = defineEmits(["inputChanged"]);
const props = defineProps({
	type: {
		type: String,
		default: "text",
	},
	name: {
		type: String,
		required: true,
	},
	rules: {
		// required: boolean
		// min: number
		type: Object,
		default: () => {},
	},
	placeholder: {
		type: String,
	},
	containerClass: {
		type: String,
		default: "",
	},
});

const isValidValue = value => {
	const { rules } = props;
	if (rules.required && value.length === 0) {
		return false;
	}

	if (rules.min && value.length < rules.min) {
		return false;
	}

	return true;
};

const validateValue = ({ target: { value } }) => {
	const { rules } = props;
	if (rules.required && value.length === 0) {
		error.value = "This value is required.";
	}

	if (rules.min && value.length < rules.min) {
		error.value = `The minimum length ${rules.min}.`;
	} else {
		error.value = null;
	}
};

const handleInputChange = $ev => {
	emit("inputChanged", {
		id: $ev.target.id,
		value: $ev.target.value,
		valid: isValidValue($ev.target.value),
	});
};
</script>

<template>
	<div :class="containerClass">
		<label
			:for="name"
			class="hidden"
			>{{ name }}</label
		>
		<div class="text-sm text-red-500">
			{{ error }}
		</div>
		<input
			:id="name"
			:type="type"
			:placeholder="placeholder"
			class="h-full w-full"
			:class="cssClasses"
			@input="handleInputChange"
			@focusout="validateValue"
		/>
	</div>
</template>
