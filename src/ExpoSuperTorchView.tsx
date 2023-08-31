import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoSuperTorchViewProps } from './ExpoSuperTorch.types';

const NativeView: React.ComponentType<ExpoSuperTorchViewProps> =
  requireNativeViewManager('ExpoSuperTorch');

export default function ExpoSuperTorchView(props: ExpoSuperTorchViewProps) {
  return <NativeView {...props} />;
}
