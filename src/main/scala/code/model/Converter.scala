package code.model

import net.liftweb.json.JsonAST.JValue

/**
 * This trait is to implemented when a new models are created
 *
 * @version 1.0
 * @author Benjamin Lüdicke
 */
trait Converter {
  /**
   * Transform the model to JSON
   */
  def objectToJValue : JValue
}