<script setup>
	import { reactive, onMounted } from "vue";
	import { useUserStore } from "@/stores/user";
	import {
		postNewListing,
		postImagesOfListing,
		deleteListing,
		getAllCategories,
	} from "@/utils/axios";
	import useNotification from "@/composables/useNotification";
	import useForm from "@/composables/useForm";
	import SelectInput from "@/components/Inputs/SelectInput.vue";
	import StarsInput from "@/components/Inputs/StarsInput.vue";
	import ImageInputList from "@/components/Inputs/ImageInputList.vue";
	import FormButton from "@/components/Inputs/FormButton.vue";
	import NotificationBox from "@/components/NotificationBox.vue";
	import TextAreaWithCounter from "@/components/Inputs/TextAreaWithCounter.vue";

	const categories = reactive([]);
	const user = useUserStore();
	const { showMsg, msgColor, displayMsg } = useNotification();
	const {
		formData,
		decorateFormData,
		handleTextInput,
		validateTextInput,
		validateNumericInput,
		handleNumericInput,
		handleMediaUpdate,
		handleSelectUpdate,
		handleConditionUpdate,
		removeNullsFromImages,
		isValidForm,
	} = useForm();

	onMounted(() => {
		fetchCategories();
		decorateFormData([
			"name",
			"detail",
			"category",
			"price",
			"condition",
			"stock",
			"media",
		]);
		formData.name.min = 1;
		formData.detail.min = 1;
		formData.detail.max = 500;
		formData.price.min = 0;
		formData.condition.min = 0;
		formData.stock.min = 0;
	});

	const fetchCategories = () => {
		getAllCategories().then(response => {
			const { data, error } = response;
			if (error || !data) return;

			data.forEach(category => {
				if (category.active) {
					categories.push(category);
				}
			});
		});
	};

	const validateTextField = $event => {
		const isValid = validateTextInput(
			$event.target.id,
			$event.target.value
		);
		if (!isValid) {
			displayMsg(
				`The ${$event.target.id} doesn't have an appropriate length`,
				"red"
			);
		}
	};

	const validateNumericField = $event => {
		const isValid = validateNumericInput(
			$event.target.id,
			$event.target.value
		);
		if (!isValid) {
			displayMsg(
				`The ${$event.target.id} doesn't have an appropriate value`,
				"red"
			);
		}
	};

	const handleFormSubmit = async () => {
		if (!isValidForm()) {
			displayMsg("All fields are obligatory", "red");
			return;
		}

		const { error, msg, newListingId } = await createNewListing();
		if (error) {
			displayMsg(msg, "red");
			return;
		}

		const { error: imgError, msg: imgMsg } = await postImages(newListingId);
		if (imgError) {
			displayMsg(imgMsg, "red");
			return;
		}

		displayMsg("Successfull operation", "green");
		// TODO redirect to other page
	};

	const organizePostPetition = () => ({
		seller_id: user.id,
		category_id: formData.category.value,
		name: formData.name.value,
		detail: formData.detail.value,
		condition: formData.condition.value,
		price: formData.price.value,
		stock: formData.stock.value,
	});

	const createNewListing = async () => {
		const data = organizePostPetition();

		const { response, error, msg } = await postNewListing(data);
		if (error) return { msg, error };

		return { newListingId: response.data.id };
	};

	const postImages = async id => {
		const images = removeNullsFromImages(formData.media.value);
		const { error, msg } = await postImagesOfListing(id, images);

		if (error) {
			await deleteListing(id);
			return { msg, error };
		}

		return {};
	};
</script>

<template>
	<main class="mb-10 ml-8 mt-6">
		<h1>Publish new product</h1>
		<form
			class="mx-10 my-5 grid w-3/4 grid-cols-2 gap-2"
			@submit.prevent="handleFormSubmit"
		>
			<input
				id="name"
				placeholder="Product name"
				title="Product name"
				class="col-span-2 rounded border border-zinc-400 px-2 py-3"
				autocomplete="off"
				@input="handleTextInput"
				@focusout="validateTextField"
			/>
			<text-area-with-counter
				name="detail"
				placeholder="Product detail"
				class="col-span-2"
				@input-changed="handleTextInput"
				@focusout="validateTextField"
			/>
			<select-input
				id="category"
				title="Category"
				:options="categories"
				@selection-changed="handleSelectUpdate"
			/>
			<input
				id="price"
				type="number"
				placeholder="Price"
				title="Product price"
				class="rounded border border-zinc-400 px-2 py-3"
				@input="handleNumericInput"
				@focusout="validateNumericField"
			/>
			<input
				id="stock"
				type="number"
				placeholder="Quantity"
				title="Number of units"
				class="col-span-1 rounded border border-zinc-400 px-2 py-3"
				@input="handleNumericInput"
				@focusout="validateNumericField"
			/>
			<stars-input
				label="Condition"
				:increment="0.5"
				color="#DE411B"
				@rating-updated="handleConditionUpdate"
			/>
			<image-input-list
				@update-media-list="handleMediaUpdate"
				@image-too-large-uploaded="
					displayMsg('The file is too large', 'red')
				"
			/>
			<form-button
				btn-type="submit"
				text="Publish"
			/>
			<notification-box
				:message="showMsg"
				:color="msgColor"
			/>
		</form>
	</main>
</template>
