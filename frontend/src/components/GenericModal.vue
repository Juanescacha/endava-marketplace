<script setup>
	import {
		Dialog,
		DialogPanel,
		DialogTitle,
		DialogDescription,
	} from "@headlessui/vue";

	const props = defineProps({
		title: {
			type: String,
			required: true,
		},
		description: {
			type: String,
		},
		open: {
			type: Boolean,
			default: false,
		},
	});

	const emit = defineEmits(["modal-closed"]);
</script>

<template>
	<Dialog :open="open">
		<div
			class="fixed inset-0 flex w-screen items-center justify-center bg-black/30 p-4"
			@click="emit('modal-closed')"
		>
			<div class="w-30 rounded-2xl bg-white p-5">
				<DialogPanel>
					<DialogTitle>{{ title }}</DialogTitle>
					<DialogDescription>
						<span v-if="description">
							{{ description }}
						</span>
						<slot v-else></slot>
					</DialogDescription>
					<button
						v-if="description"
						class="endava mt-2 border-0 px-3 py-2"
						@click="emit('modal-closed')"
					>
						Close
					</button>
				</DialogPanel>
			</div>
		</div>
	</Dialog>
</template>
