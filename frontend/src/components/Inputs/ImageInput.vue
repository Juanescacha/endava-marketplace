<script setup>
	import { ref } from "vue";

	const MAX_FILE_SIZE = 4000000;
	const imageObj = ref(null);
	const imagePreview = ref(null);

	const props = defineProps({
		id: {
			type: Number,
			required: true,
		},
	});
	const emit = defineEmits(["imageUploaded", "imageTooLargeUploaded"]);

	const extractIndexFromMediaString = mediaStr => mediaStr.slice(5);

	const setPreviewImage = () => {
		const reader = new FileReader();
		reader.readAsDataURL(imageObj.value);
		reader.onload = e => {
			imagePreview.value = e.target.result;
		};
	};

	const handleInputClick = () => {
		document.getElementById(`media${props.id}`).click();
	};

	const handleImageUpload = $ev => {
		let idx = extractIndexFromMediaString($ev.target.id);
		idx = Number(idx);

		if (Number.isNaN(idx) || idx < 0) {
			throw new Error(
				"Please format the media input ids as 'media<idx>'"
			);
		}

		if ($ev.target.files[0].size > MAX_FILE_SIZE) {
			emit("imageTooLargeUploaded");
			return;
		}

		[imageObj.value] = $ev.target.files;
		setPreviewImage();

		emit("imageUploaded", {
			idx,
			imageObj: imageObj.value,
		});
	};
</script>

<template>
	<input
		type="file"
		:id="'media' + id"
		class="hidden"
		title="Upload image"
		accept="image/png, image/jpeg"
		@input="handleImageUpload"
	/>
	<button
		class="h-full w-full"
		type="button"
		title="Upload image"
		@click="handleInputClick"
	>
		<img
			v-if="imageObj"
			:src="imagePreview"
			:alt="`Image ${id} of the product`"
		/>
		<div v-else>+</div>
	</button>
</template>
