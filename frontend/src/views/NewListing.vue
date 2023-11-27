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

	const categories = reactive([]);
	const formData = reactive({
		name: { value: "", valid: false },
		detail: { value: "", valid: false },
		category: { value: "", valid: false },
		price: { value: 0, valid: false },
		condition: { value: 0, valid: false },
		stock: { value: 0, valid: false },
		media: { value: "", valid: false },
	});

	const user = useUserStore();
	const { showMsg, msgColor, displayMsg } = useNotification();
	const {
		validateTextInput,
		validateNumericInput,
		handleInputUpdate,
		handleNumericInputUpdate,
		handleMediaUpdate,
		handleSelectUpdate,
		handleConditionUpdate,
		removeNullsFromImages,
		isValidForm,
	} = useForm();

	onMounted(() => {
		fetchCategories();
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

	const validateProductDetail = $event => {
		if ($event.target.value.length > 500) {
			displayMsg("The detail can't have more than 500 characters", "red");
		} else {
			validateTextInput($event, formData);
		}
	};

	const handleFormSubmit = async () => {
		if (!isValidForm(formData)) {
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

	const organizePostPetition = () => {
		const postData = {
			seller_id: user.id,
			category_id: formData.category.value,
			name: formData.name.value,
			detail: formData.detail.value,
			condition: formData.condition.value,
			price: formData.price.value,
			stock: formData.stock.value,
		};
		return postData;
	};

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
				@input="handleInputUpdate($event, formData)"
				@focusout="validateTextInput($event, formData)"
			/>
			<textarea
				id="detail"
				placeholder="Product detail"
				title="Product detail"
				class="col-span-2 rounded border border-zinc-400 px-2 py-3"
				@input="handleInputUpdate($event, formData)"
				@focusout="validateProductDetail"
			></textarea>
			<select-input
				id="category"
				title="Category"
				:options="categories"
				@selection-changed="handleSelectUpdate($event, formData)"
			/>
			<input
				id="price"
				type="number"
				placeholder="Price"
				title="Product price"
				class="rounded border border-zinc-400 px-2 py-3"
				@input="handleNumericInputUpdate($event, formData)"
				@focusout="validateNumericInput($event, formData)"
			/>
			<input
				id="stock"
				type="number"
				placeholder="Quantity"
				title="Number of units"
				class="col-span-1 rounded border border-zinc-400 px-2 py-3"
				@input="handleNumericInputUpdate($event, formData)"
				@focusout="validateNumericInput($event, formData)"
			/>
			<stars-input
				label="Condition"
				:increment="0.5"
				color="#DE411B"
				@rating-updated="handleConditionUpdate($event, formData)"
			/>
			<image-input-list
				@update-media-list="handleMediaUpdate($event, formData)"
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
