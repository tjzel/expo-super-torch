import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoSuperTorch.web.ts
// and on native platforms to ExpoSuperTorch.ts
import ExpoSuperTorchModule from './ExpoSuperTorchModule';
import ExpoSuperTorchView from './ExpoSuperTorchView';
import { ChangeEventPayload, ExpoSuperTorchViewProps } from './ExpoSuperTorch.types';

// Get the native constant value.
export const PI = ExpoSuperTorchModule.PI;

export function hello(): string {
  return ExpoSuperTorchModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoSuperTorchModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoSuperTorchModule ?? NativeModulesProxy.ExpoSuperTorch);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoSuperTorchView, ExpoSuperTorchViewProps, ChangeEventPayload };
