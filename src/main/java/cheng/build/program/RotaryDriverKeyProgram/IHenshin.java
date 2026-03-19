package cheng.build.program.RotaryDriverKeyProgram;

public interface IHenshin {
    /**
     * (变身前)开始摇杆
     */
    void startHenshin();

    /**
     * (变身后)结束摇杆 **主要功能**
     */
    void stopHenshin();

    /**
     * (变身后)开始摇杆
     */
    void startRound();

    /**
     * (变身后)结束摇杆 **主要功能**
     */
    void stopRound();
}
