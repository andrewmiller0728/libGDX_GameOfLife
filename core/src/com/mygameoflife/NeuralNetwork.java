package com.mygameoflife;

import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork {

    private int ID;
    private ArrayList<Perceptron> inputNodes, outputNodes;
    private ArrayList<ArrayList<Perceptron>> layers;

    public NeuralNetwork(int id,
                         int inputCount,
                         int outputCount,
                         int hiddenLayerCount,
                         int hiddenLayerSize) {
        ID = id;

        layers = new ArrayList<>();

        inputNodes = new ArrayList<>();
        for (int i = 0; i < inputCount; i++) {
            inputNodes.add(new Perceptron(id, 1));
        }
        layers.add(inputNodes);

        for (int i = 0; i < hiddenLayerCount; i++) {
            ArrayList<Perceptron> layer = new ArrayList<>();
            for (int j = 0; j < hiddenLayerSize; j++) {
                layer.add(new Perceptron(id, layers.get(i).size()));
            }
            layers.add(layer);
        }

        outputNodes = new ArrayList<>();
        for (int i = 0; i < outputCount; i++) {
            outputNodes.add(new Perceptron(id, layers.get(layers.size() - 1).size()));
        }
        layers.add(outputNodes);
    }

    public NeuralNetwork(int id, ArrayList<ArrayList<Perceptron>> layers) {
        this.ID = id;
        this.layers = new ArrayList<>();
        for (int i = 0; i < layers.size(); i++) {
            this.layers.add(new ArrayList<Perceptron>());
            for (int j = 0; j < layers.get(i).size(); j++) {
                this.layers.get(i).add(layers.get(i).get(j).getCopy());
            }
        }
        this.inputNodes = this.layers.get(0);
        this.outputNodes = this.layers.get(this.layers.size() - 1);
    }

    public double[] getOutputs(double[] inputs) {
        double[] layerOutputs = null;
        double[] prevLayerOutputs = null;
        for (ArrayList<Perceptron> currLayer : layers) {
            layerOutputs = new double[currLayer.size()];
            for (int j = 0; j < currLayer.size(); j++) {
                if (prevLayerOutputs == null) {
                    layerOutputs[j] = currLayer.get(j).getOutput(inputs[j]);
                }
                else {
                    layerOutputs[j] = currLayer.get(j).getOutput(prevLayerOutputs);
                }
            }
            prevLayerOutputs = Arrays.copyOf(layerOutputs, layerOutputs.length);
        }
        return Arrays.copyOf(layerOutputs, layerOutputs.length);
    }

    public int getID() {
        return ID;
    }

    public ArrayList<ArrayList<Perceptron>> getLayers() {
        return layers;
    }

    public NeuralNetwork getCopy() {
        return new NeuralNetwork(this.ID, this.layers);
    }
}