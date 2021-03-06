package com.feed_the_beast.ftblib.lib.io;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.IResource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author LatvianModder
 */
@SideOnly(Side.CLIENT)
public class ResourceDataReader extends DataReader
{
	public final IResource resource;

	ResourceDataReader(IResource r)
	{
		resource = r;
	}

	public String toString()
	{
		return resource.getResourceLocation().toString();
	}

	@Override
	public String string(int bufferSize) throws Exception
	{
		return readStringFromStream(resource.getInputStream(), bufferSize);
	}

	@Override
	public List<String> stringList() throws Exception
	{
		return readStringListFromStream(resource.getInputStream());
	}

	@Override
	public JsonElement json()
	{
		return JsonUtils.PARSER.parse(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
	}

	@Override
	public BufferedImage image() throws Exception
	{
		return ImageIO.read(resource.getInputStream());
	}
}