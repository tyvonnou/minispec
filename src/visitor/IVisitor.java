package visitor;

import model.Attribute;
import model.Entity;
import model.Model;

public interface IVisitor
{
  public void visitModel(Model model);

  public void visitEntity(Entity entity);

  public void visitAttribute(Attribute attribute);
}