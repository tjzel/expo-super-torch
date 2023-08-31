import React from "react";
import { Button, StyleSheet, Text, View } from "react-native";

import * as ExpoSuperTorch from "expo-super-torch";

export default function App() {
  const [torchIntensity, setTorchIntensity] = React.useState(0);
  async function handleTorchIntensity() {
    try {
      const torchIntensity = await ExpoSuperTorch.getTorchIntensity();
      setTorchIntensity(torchIntensity);
    } catch (error: any) {
      setTorchIntensity(error.toString());
      console.log(error.toString());
    }
  }


  return (
    <View style={styles.container}>
      <Button title="Fire Torch" onPress={ExpoSuperTorch.fireTorch} />
      <Button title="Stop torch" onPress={ExpoSuperTorch.stopTorch} />
      <Button title="Toggle torch (WIP)" onPress={ExpoSuperTorch.toggleTorch} />
      <Button title="Get torch intensity" onPress={handleTorchIntensity} />
      <Text>{torchIntensity}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
