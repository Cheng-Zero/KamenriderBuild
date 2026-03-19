package cheng.build.keybingds;

import net.minecraft.network.FriendlyByteBuf;

public abstract class ModKeybindings {
    private final int type;
    private final int pressedms;

    protected ModKeybindings(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    protected ModKeybindings(FriendlyByteBuf buf){
        this(buf.readInt(),buf.readInt());
    }

    protected void encode(FriendlyByteBuf buffer){
        buffer.writeInt(type);
        buffer.writeInt(pressedms);
    }

    public int getType() {
        return type;
    }
    public int getPressedms() {
        return pressedms;
    }
}
