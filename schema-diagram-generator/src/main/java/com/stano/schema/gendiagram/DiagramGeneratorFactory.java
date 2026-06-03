package com.stano.schema.gendiagram;

import com.stano.schema.gendiagram.impl.MermaidERDiagramGenerator;
import com.stano.schema.gendiagram.impl.PlantUMLERDiagramGenerator;

public class DiagramGeneratorFactory {
  public DiagramGenerator createDiagramGenerator(DiagramGeneratorOptions options) {
    return switch (options.getFormat()) {
      case MERMAID -> new MermaidERDiagramGenerator(options);
      case PLANTUML -> new PlantUMLERDiagramGenerator(options);
    };
  }
}
