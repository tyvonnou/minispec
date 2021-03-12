package visitor;

import java.io.IOException;

public interface IVisitable {
	public void accept(IVisitor visitor) throws IOException;
}
