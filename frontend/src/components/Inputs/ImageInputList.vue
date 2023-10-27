<script setup>
	import { ref, reactive, computed } from "vue";
	import ImageInput from "./ImageInput.vue";

	const amountOfInputs = ref(1);
	const media = reactive([null]);
	const emit = defineEmits(["updateMediaList", "imageTooLargeUploaded"]);

	const props = defineProps({
		maxAmountOfInputs: {
			type: Number,
			default: 5,
		},
	});

	const displayMediaLength = computed(() =>
		media.includes(null) ? media.length - 1 : media.length
	);

	const updateAmountOfInputs = idx => {
		if (
			idx === media.length - 1 &&
			amountOfInputs.value < props.maxAmountOfInputs
		) {
			amountOfInputs.value += 1;
			media.push(null);
		}
	};

	const handleMediaInput = ({ idx, imageObj }) => {
		media[idx] = imageObj;
		emit("updateMediaList", media);
		updateAmountOfInputs(idx);
	};

	const handleLargeFile = () => {
		emit("imageTooLargeUploaded");
	};
</script>

<template>
	<div class="col-span-2">
		<span>{{ `Images (${displayMediaLength}/${maxAmountOfInputs})` }}</span>
		<ul
			class="flex gap-4"
			id="media"
		>
			<li
				v-for="(num, i) in amountOfInputs"
				:key="'media' + i"
				class="h-20 w-40 overflow-hidden rounded-sm border border-zinc-400"
			>
				<image-input
					:id="i"
					@image-uploaded="handleMediaInput"
					@image-too-large-uploaded="handleLargeFile"
				/>
			</li>
		</ul>
	</div>
</template>
