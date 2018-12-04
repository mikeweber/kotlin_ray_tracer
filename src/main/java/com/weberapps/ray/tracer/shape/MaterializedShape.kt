package com.weberapps.ray.tracer.shape

import com.weberapps.ray.tracer.material.Material

interface MaterializedShape: Shape {
  var material: Material
}